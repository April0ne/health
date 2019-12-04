package com.aprilhealth.controller;
/*
 *@author April0ne
 *@date 2019/11/25 20:20
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.aprilhealth.constant.MessageConstant;
import com.aprilhealth.entity.Result;
import com.aprilhealth.service.SetmealService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/setmealReport")
@PreAuthorize("hasAnyAuthority('REPORT_VIEW')")
public class SetmealReportController {

    @Reference
    private SetmealService setmealService;

    /**
     * 套餐的预约占比;
     *      目前不考虑时间范围,不考虑订单状态,不考虑是否就诊等状态;查询全部时间范围的所有订单;
     *      占比 = 订单数 / 某套餐订单数
     * @return
     * @throws Exception
     */
    @GetMapping("/getSetmealReservationRatio")
    public Result getSetmealReservationRatio() throws Exception{
        Result result = null;
        //封装setmealNames数据
        List<String> setmealNames = new ArrayList<>();
        //封装上面两个数据
        Map<String,Object> map = new HashMap();//map中存储的key:setmealNames,setmealCount;值:套餐名称,套餐值
        try {
            List<Map<String, Object>> setmealCountMap = setmealService.findSetmealReservationRatio();
            for (Map<String, Object> keyNameMap : setmealCountMap) {
                String name = (String)keyNameMap.get("name");
                setmealNames.add(name);
            }
            map.put("setmealNames",setmealNames);
            map.put("setmealCount",setmealCountMap);
            result = new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
        return result;
    }
}
