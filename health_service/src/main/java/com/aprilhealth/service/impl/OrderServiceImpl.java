package com.aprilhealth.service.impl;
/*
 *@author April0ne
 *@date 2019/11/22 20:49
 */


import com.alibaba.dubbo.config.annotation.Service;
import com.aprilhealth.constant.MessageConstant;
import com.aprilhealth.dao.MemberDao;
import com.aprilhealth.dao.OrderDao;
import com.aprilhealth.dao.OrderSettingDao;
import com.aprilhealth.dao.SetmealDao;
import com.aprilhealth.entity.Result;
import com.aprilhealth.pojo.Member;
import com.aprilhealth.pojo.Order;
import com.aprilhealth.pojo.OrderSetting;
import com.aprilhealth.pojo.Setmeal;
import com.aprilhealth.service.OrderService;
import com.aprilhealth.utils.DateUtils;
import com.aprilhealth.utils.ReservationTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Transactional
@Service(protocol = "dubbo",interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private SetmealDao setmealDao;

    /**
     * 查询:用户是否已预约
     * @param order
     * @return
     */
    @Override
    public Order findOrder(Order order) {
        return null;
    }

    /**
     * 是否允许当前用户预约成功
     * @param map
     * @return
     */
    @Override
    public Result order(Map map) throws Exception{
        Result result = null;
        //会员手机号
        String telephone = (String)map.get("telephone");
        //会员预约日期
        String orderDateStr = (String) map.get("orderDate");
        Date orderDate = DateUtils.parseString2Date(orderDateStr);
        //预约的套餐id
        String setmealStr = (String)map.get("setmealId");
        Integer setmealId = Integer.parseInt(setmealStr);
        //订单
        Order order = null;


        //判断用户是否存在
        Member member = memberDao.findMemberByTel(telephone);
        if (member == null) {
            //不存在
            return result = new Result(false, MessageConstant.QUERY_MEMBER_FAIL);
        }

        //判断用户所选预约日期是否允许预约
        if (!ReservationTimeUtils.isReservation(orderDate)) {
            //不允许
            return result = new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //判断用户是否已预约
        order = new Order(member.getId(),orderDate,null,null,setmealId);
        if (haveOrder(order)) {
            //已预约
            return result = new Result(false, MessageConstant.HAS_ORDERED);
        }

        //判断用户所选日期是否已约满
        OrderSetting orderSetting = orderSettingDao.findByDate(orderDate);
        if ( (orderSetting.getNumber() - orderSetting.getReservations()) <= 0 ) {
            //已约满
            return result = new Result(false, MessageConstant.ORDER_FULL);
        }

        //可预约
        //order表新增订单
        order.setOrderType((String)map.get("orderType"));
        order.setOrderStatus(order.ORDERSTATUS_NO);
        orderDao.addOrder(order);
        //orderSetting 已预约人数+1
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        Integer isEdit = orderSettingDao.editReservationsByOrderDate(orderSetting);
        if (isEdit != null && isEdit > 0) {
            result = new Result(true, MessageConstant.ORDER_SUCCESS,order);
        }else {
            result = new Result(true, MessageConstant.ORDER_FAIL);
        }

        return result;
    }

    /**
     * 预约成功,查询预约信息
     * @param orderId
     * @return
     */
    @Override
    public Map findById(Integer orderId) {
        Map map = new HashMap();
        Order order = orderDao.findOrderById(new Order(orderId));
        //查询套餐名称,给前端返回预约信息
        System.out.println(order);
        Integer sId = order.getSetmealId();
        Setmeal setmeal = setmealDao.findById(order.getSetmealId());
        System.out.println(setmeal);
        //查询预约人姓名
        Integer mId = order.getMember_id();
        Member member = memberDao.findMemberById(order.getMember_id());
        System.out.println(member);
        //封装前端要展示的预约信息
        map.put("setmealName",setmeal.getName());
        map.put("orderDate",order.getOrderDate());
        map.put("member",member.getName());
        map.put("orderType",order.getOrderType());
        return map;
    }

    /**
     * 判断用户是否已预约
     * @param order
     * @return
     */
    public boolean haveOrder(Order order ){
        //判断用户是否已预约
        Order isOrder = orderDao.findOrder(order);
        if (isOrder == null) {
            //没有预约
            return false;
        }
        //已预约
        return true;
    }


}
