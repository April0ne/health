package com.aprilhealth.service;
/*
 *@author April0ne
 *@date 2019/11/26 13:50
 */


import java.util.Map;

public interface ReportService {
    /**
     * 查询运营数据
     */
    Map<String,Object> getBusinessData()throws Exception ;
}
