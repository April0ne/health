package com.aprilhealth.dao;
/*
 *@author April0ne
 *@date 2019/11/14 15:22
 *处理检查组操作dao的需求
 */

import com.aprilhealth.pojo.OrderSetting;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface OrderSettingDao {

    /**
     * 根据日期查询预约信息
     * @param orderDate
     * @return
     */
    @Select("select * from t_ordersetting where orderDate = #{orderDate}")
    OrderSetting findByDate(Date orderDate);

    /**
     * 设置(新增)预约信息
     * @param orderSetting
     * @return
     */
    @Insert("insert into t_ordersetting (orderDate,number,reservations) values (#{orderDate},#{number},#{reservations})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()",keyProperty = "id",before = false,resultType = int.class)
    Integer addOrderSetting(OrderSetting orderSetting);

    /**
     * 根据日期更新可预约人数
     * @param orderSetting
     * @return
     */
    @Update("update t_ordersetting set number = #{number} where orderDate = #{orderDate}")
    Integer updateByData(OrderSetting orderSetting);

    /**
     * 根据日期更新已预约人数
     * @param orderSetting
     * @return
     */
    @Update("update t_ordersetting set reservations = #{reservations} where orderDate = #{orderDate}")
    Integer editReservationsByOrderDate(OrderSetting orderSetting);


    /**
     * 模糊查询:根据时间
     * @param orderDate
     * @return
     */
    @Select("select * from t_ordersetting where orderDate like #{value}")
    List<OrderSetting> findLikeByDate(String orderDate);

}
