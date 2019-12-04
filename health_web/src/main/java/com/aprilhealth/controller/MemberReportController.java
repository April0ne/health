package com.aprilhealth.controller;
/*
 *@author April0ne
 *@date 2019/11/25 14:59
 */


import com.alibaba.dubbo.config.annotation.Reference;
import com.aprilhealth.constant.MessageConstant;
import com.aprilhealth.entity.Result;
import com.aprilhealth.service.MemberReportService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/userReport")
public class MemberReportController {

    @Reference
    private MemberReportService memberReportService;

    /**
     * 会员数量报表
     * @return
     * @throws Exception
     */
    @GetMapping("/getMemberGrowthReport")
    @PreAuthorize("hasAnyAuthority('REPORT_VIEW')")
    public Result getMemberGrowthReport() throws Exception{
        Result result = null;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);//在当前月份的基础上,减去12个月,获取减去12个月分后的日期.

        //将要查询会员数的月份封装成list
        List<String> list = new ArrayList<>();
        for (int m = 0;m < 12;m++) {
            calendar.add(Calendar.MONTH,1);//获取当前月的前一m个月
            list.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }

        //将要查询会员数的月份封装的list,存储至map
        Map<String,Object> map = new HashMap<>();
        map.put("months",list);

        try {
            //查询月份对应的会员注册数,封装成list == 为什么是list,不是long? 每个月份的会员注册数都存储在list中
            List<Long> memberCount = memberReportService.findMemberCountByMonth(list);
            //将要查询会员数的月份对应的会员数,存储至map
            map.put("memberCount",memberCount);
            result  = new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            result  = new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }

        return result;
    }
}
