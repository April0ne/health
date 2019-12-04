package com.aprilhealth.service.impl;
/*
 *@author April0ne
 *@date 2019/11/14 20:43
 *处理检查组业务的接口的实现类
 */


import com.alibaba.dubbo.config.annotation.Service;
import com.aprilhealth.constant.RedisConstant;
import com.aprilhealth.dao.CheckGroupDao;
import com.aprilhealth.dao.CheckItemDao;
import com.aprilhealth.dao.SetmealDao;
import com.aprilhealth.entity.PageResult;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.CheckGroup;
import com.aprilhealth.pojo.CheckItem;
import com.aprilhealth.pojo.Setmeal;
import com.aprilhealth.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service(protocol = "dubbo",interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Autowired
    private CheckItemDao checkItemDao;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 分页查询(使用分页插件)
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPageFromSetmeal(QueryPageBean queryPageBean) throws RuntimeException{
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Setmeal> pageSetmeal = setmealDao.findPageFromSetmeal(queryPageBean.getQueryString());
        return new PageResult(pageSetmeal.getTotal(),pageSetmeal.getResult());
    }

    /**
     * 查询所有检查组数据
     * @return
     */
    @Override
    public List<CheckGroup> findAllCheckGroup() throws RuntimeException{
        return checkGroupDao.findAllCheckGroup();
    }

    /**
     * 新增套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @Override
    public boolean addSetmeal(Setmeal setmeal,Integer[] checkgroupIds) throws RuntimeException{
        if (setmealDao.addSetmeal(setmeal) > 0) {
            if (addToT_setmeal_checkgroup(setmeal.getId(), checkgroupIds)) {
                //数据库新增套餐成功,将图片名称存储至redis
                jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
                return true;
            }
        }
        return false;
    }


    /**
     * 新增关联表数据(t_setmeal_checkgroup)
     * @param setmealId
     * @param checkgroupIds
     * @return
     */
    @Override
    public boolean addToT_setmeal_checkgroup(Integer setmealId, Integer[] checkgroupIds) throws RuntimeException{
        boolean result = false;
        if (setmealId != null && setmealId > 0) {
            if (checkgroupIds != null && checkgroupIds.length > 0) {
                for (Integer checkgroupId : checkgroupIds) {
                        Map<String,Integer> map = new HashMap<>();
                        map.put("setmeal_id",setmealId);
                        map.put("checkgroup_id",checkgroupId);
                        setmealDao.addToT_setmeal_checkgroup(map);

                }
            }
            result = true;
        }
        return result;
    }

    /**
     * mobile : 查询所有套餐
     * @return
     */
    @Override
    public List<Setmeal> findAllSetmeal()throws RuntimeException {
        return setmealDao.findAllSetmeal();
    }

    /**
     * mobile : 查询套餐之ByID:查看套餐详情
     * @param setmeal_id
     * @return
     */
    @Override
    public Setmeal findById(Integer setmeal_id) throws RuntimeException{
        //封装套餐内容
        Setmeal setmeal = setmealDao.findById(setmeal_id);
        //获得检查组list
        List<CheckGroup> checkGroupList = getCheckGroupList(setmeal_id);
        setmeal.setCheckGroups(checkGroupList);
        return setmeal;
    }

    /*
     * 套餐的预约占比;
     *      目前不考虑时间范围,不考虑订单状态,不考虑是否就诊等状态;查询全部时间范围的所有订单;
     *      占比 = 订单数 / 某套餐订单数
     */
    @Override
    public List<Map<String,Object>> findSetmealReservationRatio() {
        return setmealDao.findSetmealReservation();
    }


    /**
     * 获取检查组list
     * @param setmeal_id
     * @return
     */
    public List<CheckGroup> getCheckGroupList(Integer setmeal_id)throws RuntimeException{
        //封装检查组list
        List<CheckGroup> checkGroupList = new ArrayList<>();
        //获取检查组list
        List<Integer> checkGroupIDList = checkGroupDao.findCheckGroupIDBySetmealId(setmeal_id);
        for (Integer checkGroupID : checkGroupIDList) {
            if (checkGroupID != null && checkGroupID != 0) {
                CheckGroup oneCheckGroup = checkGroupDao.findOneCheckGroup(checkGroupID);
                //获得检查项list
                List<CheckItem> checkItemList = getCheckItemList(checkGroupID);
                oneCheckGroup.setCheckItems(checkItemList);
                checkGroupList.add(oneCheckGroup);
            }
        }

        return checkGroupList;

    }

    /**
     * 封装检查项list
     * @param checkGroup_id
     * @return
     */
    public List<CheckItem> getCheckItemList(Integer checkGroup_id)throws RuntimeException{
        //封装检查项list
        List<CheckItem> checkItemList = new ArrayList<>();
        //获取检查项list
        List<Integer> checkItemIDList = checkItemDao.findCheckItemFromT_checkgroup_checkitem(checkGroup_id);
        for (Integer checkItemID : checkItemIDList) {
            if (checkItemID != null && checkItemID != 0) {
                CheckItem oneCheckItem = checkItemDao.findOneCheckItem(checkItemID);
                checkItemList.add(oneCheckItem);
            }
        }
        return checkItemList;
    }

}