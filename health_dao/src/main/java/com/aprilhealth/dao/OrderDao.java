package com.aprilhealth.dao;
/*
 *@author April0ne
 *@date 2019/11/22 21:22
 */


import com.aprilhealth.pojo.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface OrderDao {

    /**
     * 查询订单 : 根据日期,用户id,套餐id
     * @param order
     * @return
     */
    @Select("select * from t_order where orderDate = #{orderDate} and setmeal_id = #{setmealId} and member_id = #{member_id}")
    Order findOrder(Order order);

    /**
     * 新增订单
     * @param order
     * @return
     */
    @Insert("insert into t_order (member_id,orderDate,orderType,orderStatus,setmeal_id) values(#{member_id},#{orderDate},#{orderType},#{orderStatus},#{setmealId})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()",before = false,keyProperty = "id",resultType = Integer.class)
    Integer addOrder(Order order);

    /**
     * 查询订单 : 根据订单id
     * @param order
     * @return
     */
    @Select("select id,member_id,orderDate,orderType,orderStatus,setmeal_id as setmealId from t_order where id = #{id}")
    Order findOrderById(Order order);

    /**
     * 查询昨天订单数
     * @param yesterday
     * @return
     */
    @Select("SELECT count(id) FROM t_order where orderDate = #{value}")
    Long findYesterdayOrder(String yesterday);

    /**
     * 查询某时间段的订单数
     * @param firstDay
     * @param lastDay
     * @return
     */
    @Select("SELECT count(id) FROM t_order  where orderDate >= #{firstDay}  and   orderDate <= #{lastDay}")
    Long findCountOrderBetweenDate(@Param(value = "firstDay")String firstDay, @Param(value = "lastDay")String lastDay);

    /**
     * 查询昨天已就诊订单数
     * @param orderDate
     * @param Status
     * @return
     */
    @Select("SELECT count(id) FROM t_order where orderDate = #{orderDate} and orderStatus = #{Status}")
    Long findYesterdayOrderStatus(@Param(value = "orderDate") String orderDate,@Param(value = "Status") String Status);

    /**
     * 查询某时间段的就诊订单数
     * @param firstDay
     * @param lastDay
     * @param Status
     * @return
     */
    @Select("SELECT count(id) FROM t_order  where orderDate >= #{firstDay}  and   orderDate <= #{lastDay} and orderStatus = #{Status}")
    Long findOrderBetweenDateStatus(@Param(value = "firstDay")String firstDay,  @Param(value = "lastDay")String lastDay, @Param(value = "Status")String Status);


    /**
     * 查询所有订单
     * @return
     */
    @Select("SELECT count(setmeal_id) setmeal_count  from  t_order")
    Long findAll();


    /**
     * 查询热门套餐
     * @return
     */
    @Select("select ts.`name`,count(tr.setmeal_id) as setmeal_count,count(tr.setmeal_id)/(select count(id) from t_order) as proportion  from t_order tr,t_setmeal ts WHERE tr.setmeal_id = ts.id GROUP BY tr.setmeal_id order by tr.setmeal_id desc ")
    List<Map<String,Object>> findHotSetmeal();
}
