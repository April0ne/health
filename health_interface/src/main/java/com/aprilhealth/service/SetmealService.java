package com.aprilhealth.service;
/*
 *@author April0ne
 *@date 2019/11/14 20:44
 *处理检查组业务的接口
 */


import com.aprilhealth.entity.PageResult;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.pojo.CheckGroup;
import com.aprilhealth.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {

    /**
     * 分页查询(使用分页插件)
     * @param queryPageBean
     * @return
     */
    PageResult findPageFromSetmeal(QueryPageBean queryPageBean) throws RuntimeException;

    /**
     * 查询所有检查组数据
     * @return
     */
    List<CheckGroup> findAllCheckGroup()throws RuntimeException;

    /**
     * 新增套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    boolean addSetmeal(Setmeal setmeal,Integer[] checkgroupIds)throws RuntimeException;

    /**
     * 新增关联表数据(t_setmeal_checkgroup)
     * @param setmealId
     * @param checkgroupIds
     * @return
     */
    boolean addToT_setmeal_checkgroup(Integer setmealId,Integer[] checkgroupIds)throws RuntimeException;


    /**
     * mobile : 查询所有套餐
     * @return
     */
    List<Setmeal> findAllSetmeal()throws RuntimeException;

    /**
     * 查询之ByID:查看套餐详情
     * @param id
     * @return
     */
    Setmeal findById(Integer id)throws RuntimeException;

    /**
     *套餐的预约占比;
     *   目前不考虑时间范围,不考虑订单状态,不考虑是否就诊等状态;查询全部时间范围的所有订单;
     *   占比 = 订单数 / 某套餐订单数
     * @return
     */
    List<Map<String,Object>> findSetmealReservationRatio();
}
