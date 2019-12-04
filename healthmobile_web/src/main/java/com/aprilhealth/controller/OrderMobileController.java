package com.aprilhealth.controller;
/*
 *@author April0ne
 *@date 2019/11/22 8:46
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.aprilhealth.constant.MessageConstant;
import com.aprilhealth.constant.RedisMessageConstant;
import com.aprilhealth.entity.Result;
import com.aprilhealth.pojo.Member;
import com.aprilhealth.pojo.Order;
import com.aprilhealth.service.OrderService;
import com.aprilhealth.service.OrderSettingService;
import com.aprilhealth.utils.ReservationTimeUtils;
import com.aprilhealth.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/orderMobile")
public class OrderMobileController {

    @Reference
    private OrderService orderService;

    @Reference
    private OrderSettingService orderSettingService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 提交预约
     * @param map
     * @return
     * @throws Exception
     */
    @PostMapping("/submit")
    public Result method(@RequestBody Map map) throws Exception{
        Result result = null;
        //判断验证码是否正确
        String telephone = (String)map.get("telephone");
        if (jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER) == null) {
            //不正确
            return result = new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        try {
            map.put("orderType",Order.ORDERTYPE_WEIXIN);//这个应该由前端传过来,而不是后台写死
            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            //预约失败
            result = new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
        // 预约成功，发送短信通知，短信通知内容可以是“预约时间”，“预约人”，“预约地点”，“预约事项”等信息。=== 略
        return result;
    }

    /**
     * 预约成功,查询预约信息
     * @param orderId
     * @return
     * @throws Exception
     */
    @GetMapping("/findReservationInformation/{orderId}")
    public Result findReservationInformation(@PathVariable(value = "orderId") Integer orderId) throws Exception{
        Result result = null;
        try {
            Map map = orderService.findById(orderId);
            result =  new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            result =  new Result(true,MessageConstant.QUERY_ORDER_FAIL);
        }

        return result;
    }

}
