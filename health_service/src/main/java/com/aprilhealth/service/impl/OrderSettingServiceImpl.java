package com.aprilhealth.service.impl;
/*
 *@author April0ne
 *@date 2019/11/14 20:43
 *处理检查组业务的接口的实现类
 */

import com.alibaba.dubbo.config.annotation.Service;
import com.aprilhealth.constant.OrderDateConstant;
import com.aprilhealth.dao.OrderSettingDao;
import com.aprilhealth.entity.ExcelModuleOrderSettingFailed;
import com.aprilhealth.entity.OrderSettingFail;
import com.aprilhealth.pojo.OrderSetting;
import com.aprilhealth.service.OrderSettingService;
import com.aprilhealth.utils.DeserializationUtils;
import com.aprilhealth.utils.ReservationTimeUtils;
import com.aprilhealth.utils.SerializationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.*;

@Transactional
@Service(protocol = "dubbo",interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService{

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private JedisPool jedisPool;


    //服务器时间
    private  Date date = new Date();
    //如果是多线程就不能这样处理.
    private List<OrderSettingFail> orderSettingFails = new ArrayList<>();
    private OrderSettingFail orderSettingFail = null;

    /**
     * 查询某月整月的预约信息.
     * @param data
     * @return
     */
    @Override
    public List<Map> findMonthOrderSetting(String data) throws RuntimeException{
        data = data+"%";
        List<OrderSetting> likeByDate = orderSettingDao.findLikeByDate(data);
        List<Map> orderSettingMap = new ArrayList<>();
        //封装成map,前端获取数据需要键值对类型
        for (OrderSetting orderSetting : likeByDate) {
            Map map = new HashMap();
            map.put("date",orderSetting.getOrderDate().getDate());//获取日
            map.put("number",orderSetting.getNumber());
            map.put("reservations",orderSetting.getReservations());
            orderSettingMap.add(map);
        }
        return orderSettingMap;
    }


    /**
     * 编辑预约设置信息(新增/修改)
     * @param orderSetting
     * @return
     */
    @Override
    public boolean editOrderSetting(OrderSetting orderSetting) throws RuntimeException{
        //编辑是否成功
        boolean isEdit = false;
        //判断日期是否已存在
        boolean have = haveOrderDate(orderSetting);
        if (have) {
            //已存在
            try {
                editOneOrderSetting(orderSetting);
                if(orderSettingDao.updateByData(orderSetting) > 0){
                    isEdit = true;
                }else {
                    isEdit = false;
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e.getMessage());
            }
        }else {
            //不存在
            //直接新增
            isEdit = orderSettingDao.addOrderSetting(orderSetting) > 0 ? true:false;
        }
        return isEdit;
    }

    /**
     * 批量设置预约信息
     *      判断日期是否存在.如果存在.如果不存在.
     *          + 如果存在(更新)
     *              判断日期是否过期(和服务器对比),过期了直接不更新
     *              判断日期是否是服务器当前时间.
     *                  如果是,则查询今天已预约的人数,如已预约人数大于当前数据,则不允许修改;
     *                  如果小于当前数据,则允许修改
     *              如果日期是未来,且大于30天,则直接更新
     *              如果日期是未来,且小于30天,则查询,比较已预约人数是否比当前可预约人数大:
     *                  如果大,不允许修改
     *                  如果小允许更新.
 *              + 如果不存在(新增)
     *              判断日期是否过期(和服务器对比),不新增
     *              否则一律新增
     * @param list
     * @return
     */
    @Override
    public boolean addOrderSetting(List<OrderSetting> list) throws RuntimeException{
        boolean ifSetting = false;
        //遍历OrderSetting集合
        for (OrderSetting orderSetting : list) {
            //判断日期是否已存在
            boolean have = haveOrderDate(orderSetting);
            if (have) {
                //已存在
                Integer flag = judgeOrderDateExisted(orderSetting.getOrderDate(), orderSetting.getNumber());
                boolean b = updateOneOrderSetting(flag,orderSetting);
                if (b) {
                    ifSetting = true;
                }else {
                    ifSetting = false;
                }

            }else {
                //不存在
                Integer flag = judgeOrderDateNotExisted(orderSetting.getOrderDate());
                boolean b = addOneOrderSetting(flag,orderSetting);
                if (b) {
                    ifSetting = true;
                }else {
                    ifSetting = false;
                }
            }
        }
        return ifSetting;
    }

    /**
     * 日期不存在 : 是否新增预约信息
     * @param orderDate
     * @return 允许新增 :3;不可新增,4
     */
    @Override
    public Integer judgeOrderDateNotExisted(Date orderDate) throws RuntimeException{
        //判断日期是否过期,和服务器日期
        if (!date.before(orderDate)) {
            //历史时间不允许新增
            return OrderDateConstant.HISTORICAL_TIME_NOT_ALLOW_ADD;
        }
        //允许新增
        return OrderDateConstant.ALLOW_ADD;
    }

    /**
     * 日期已存在 : 是否更新预约信息
     * @param orderDate
     * @return 不允许更新 :0,2;可更新,1
     */
    @Override
    public Integer judgeOrderDateExisted(Date orderDate,Integer number) throws RuntimeException{
        //判断日期是否过期,和服务器日期
        if (!date.before(orderDate)) {
            //历史时间不允许修改
            return OrderDateConstant.HISTORICAL_TIME_NOT_ALLOW_UPDATES;
        }

        //判断日期是否和服务器日期相等
        if (date.equals(orderDate)) {
            //相等
            if (compareNumberAndReservations(orderDate, number)) {
                //可预约人数大于已预约人数,可更新
                return  OrderDateConstant.ALLOW_UPDATES;
            }else {
                //可预约人数小于已预约人数,不可更新
                return  OrderDateConstant.NOT_ALLOW_UPDATES;
            }
        }

        //判断日期是否可预约范围,reservationMaxTime在orderDate之前表示(true),orderDate已经在可预约的时间范围内.
        if (ReservationTimeUtils.isReservation(orderDate)) {
            //orderDate已经在可预约的时间范围内
            if (compareNumberAndReservations(orderDate, number)) {
                //可预约人数大于已预约人数,可更新
                return  OrderDateConstant.ALLOW_UPDATES;
            }else {
                //但可预约人数小于已预约人数,不可更新
                return  OrderDateConstant.NOT_ALLOW_UPDATES;
            }
        }
        //日期可以直接更新
        return OrderDateConstant.ALLOW_UPDATES;
    }


    /**
     * 日期已存在,比较number是否小于reservations
     * @param orderDate
     * @return number,小于reservations不允许修改
     */
    @Override
    public boolean compareNumberAndReservations(Date orderDate,Integer number) throws RuntimeException{
        boolean flag = false;
        //根据日期查询人数
        OrderSetting orderSettingDB = orderSettingDao.findByDate(orderDate);
        if (orderSettingDB != null) {
            Integer reservations = orderSettingDB.getReservations();
            //更新的人数是否大于已预约人数
            if ( (number - reservations) > 0) {
                flag = true;
            }
        }
        return flag;
    }


    /**
     * 设置(新增)预约信息
     * @param flag
     * @return
     */
    public boolean addOneOrderSetting(Integer flag,OrderSetting orderSetting)throws RuntimeException{
        boolean ifAdd = false;
        switch(flag){
            //批量设置预约信息,历史日期,不可新增预约信息;
            case 3 :
                ifAdd = false;
                orderSettingFail = new OrderSettingFail(orderSetting.getOrderDate(), orderSetting.getNumber(), "历史日期不允许新增");
                orderSettingFails.add(orderSettingFail);
                break;
            //批量设置预约信息,可新增预约信息;
            case 4 :
                ifAdd = orderSettingDao.addOrderSetting(orderSetting) > 0 ? true:false;
                break;
        }
        return  ifAdd;
    }

    /**
     * 更新预约信息(如果不允许更新,且将信息写入excel表)
     * @param flag
     * @param orderSetting
     * @return
     */
    public boolean updateOneOrderSetting(Integer flag,OrderSetting orderSetting)throws RuntimeException{
        boolean ifUpdate = false;
        switch(flag){
            //批量设置预约信息,历史日期,不可更新预约信息;
            case 0 :
                ifUpdate = false;
                orderSettingFail = new OrderSettingFail(orderSetting.getOrderDate(), orderSetting.getNumber(), "历史日期不允许修改");
                orderSettingFails.add(orderSettingFail);
                break;

            //批量设置预约信息,可更新预约信息;
            case 1 :
                ifUpdate = orderSettingDao.updateByData(orderSetting) > 0 ? true:false;
                break;

            //批量设置预约信息,不可更新预约信息;
            case 2 :
                ifUpdate = false;
                orderSettingFail = new OrderSettingFail(orderSetting.getOrderDate(), orderSetting.getNumber(), "可预约人数小于已预约人数,不可修改");
                orderSettingFails.add(orderSettingFail);
                break;
        }

        return ifUpdate;
    }


    /**
     * 获得设置导出的excel模板信息
     * @return
     */
    @Override
    public boolean setExcelModule()throws RuntimeException{

        List<String> headers = new ArrayList<>();
        headers.add("日期");
        headers.add("可预约人数");
        headers.add("设置失败的原因");
        boolean isExcel2003 = false;
        String sheetName = "批量导入失败数据";
        //如果所有数据都写入数据库则无需写出至excel
        if (orderSettingFails != null && orderSettingFails.size() > 0) {
            ExcelModuleOrderSettingFailed excelModuleOrderSettingFailed = new ExcelModuleOrderSettingFailed(headers, orderSettingFails, isExcel2003, sheetName);
            //将数据存储至redis
            jedisPool.getResource().set(OrderDateConstant.REDIS_KEY_SAVE_EXCEL.getBytes(), SerializationUtil.serialization(excelModuleOrderSettingFailed));
            return true;
        }
        return false;
    }


    /**
     * 从redis中获取excel模板信息
     * @param StrOrderSettingFailed
     * @return
     */
    @Override
    public ExcelModuleOrderSettingFailed getOrderSettingFailed(String StrOrderSettingFailed) throws RuntimeException{
        if (StrOrderSettingFailed != null && !"".equals(StrOrderSettingFailed) ) {
            byte[] bytesOrderSettingFailed = jedisPool.getResource().get(StrOrderSettingFailed.getBytes());
            return (ExcelModuleOrderSettingFailed) DeserializationUtils.deserialize(bytesOrderSettingFailed);
        }
        return null;
    }

    /**
     * 判断日期是否已经存在
     * @param orderSetting
     */
    public boolean haveOrderDate(OrderSetting orderSetting)throws RuntimeException{
        Date orderDate = orderSetting.getOrderDate();
        Integer number = orderSetting.getNumber();
        //根据日期查询人数
        OrderSetting orderSettingDB = orderSettingDao.findByDate(orderDate);
        //判断日期是否已存在
        if (orderSettingDB != null) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 编辑预约信息(实质是更新)
     * @return
     */
    public boolean editOneOrderSetting(OrderSetting orderSetting)throws RuntimeException{
        //判断日期是否和服务器日期相等
        if (date.equals(orderSetting.getOrderDate())) {
            //相等
            if (compareNumberAndReservations(orderSetting.getOrderDate(), orderSetting.getNumber())) {
                //可预约人数大于已预约人数,可更新
                return true;
            }else {
                //但可预约人数小于已预约人数,不可更新
                throw new RuntimeException("可预约人数小于已预约人数,不可更新");
//                throw  new OrderSettingException ();
            }

        }

        //判断日期是否可预约范围,reservationMaxTime在orderDate之前表示(true),orderDate已经在可预约的时间范围内.
        if (ReservationTimeUtils.isReservation(orderSetting.getOrderDate())){
            //orderDate已经在可预约的时间范围内
            if (compareNumberAndReservations(orderSetting.getOrderDate(), orderSetting.getNumber())) {
                //可预约人数大于已预约人数,可更新
                return true;
            }else {
                //但可预约人数小于已预约人数,不可更新
                throw new RuntimeException("可预约人数小于已预约人数,不可更新");
            }
        }
        //日期可以直接更新
        return true;
    }

}
