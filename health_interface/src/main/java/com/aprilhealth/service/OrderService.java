package com.aprilhealth.service;
/*
 *@author April0ne
 *@date 2019/11/22 20:50
 */


import com.aprilhealth.entity.Result;
import com.aprilhealth.pojo.Member;
import com.aprilhealth.pojo.Order;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface OrderService {

    /**
     * 查询:用户是否已预约
     * @param order
     * @return
     */
    Order findOrder(Order order);

    /**
     * 是否允许当前用户预约成功
     * @param map
     * @return
     */
    Result order(Map map) throws Exception;

    /**
     * 预约成功,查询预约信息
     * @param orderId
     * @return
     */
    Map findById(Integer orderId);
}
