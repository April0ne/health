package com.aprilhealth.service;
/*
 *@author April0ne
 *@date 2019/11/19 19:46
 */


import com.aprilhealth.entity.ExcelModuleOrderSettingFailed;
import com.aprilhealth.entity.Result;
import com.aprilhealth.pojo.Member;
import com.aprilhealth.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    /**
     * 设置预约信息
     * @param list
     * @return
     */
    boolean addOrderSetting(List<OrderSetting> list)throws RuntimeException;

    /**
     * 日期已存在 : 是否更新预约信息
     * @param orderDate
     * @param number
     * @return
     */
    Integer judgeOrderDateExisted(Date orderDate,Integer number)throws RuntimeException;

    /**
     * 日期不存在 : 是否新增预约信息
     * @param orderDate
     * @return
     */
    Integer judgeOrderDateNotExisted(Date orderDate)throws RuntimeException;

    /**
     * 日期已存在,比较number是否小于reservations
     * @param orderDate
     * @param number
     * @return
     */
    boolean compareNumberAndReservations(Date orderDate,Integer number)throws RuntimeException;

    /**
     * 获得设置导出的excel模板信息,并将OrderSettingFailed存储至redis
     * @return
     */
    boolean setExcelModule()throws RuntimeException;

    /**
     * 获得某月整月的预约信息.
     * @param data
     * @return
     */
    List<Map> findMonthOrderSetting(String data)throws RuntimeException;

    /**
     * 编辑预约设置信息(新增/修改)
     * @param orderSetting
     * @return
     */
    boolean editOrderSetting(OrderSetting orderSetting)throws RuntimeException;

    /**
     * 从redis中获取excel模板信息
     * @param bytesOrderSettingFailed
     * @return
     */
    ExcelModuleOrderSettingFailed getOrderSettingFailed(String bytesOrderSettingFailed)throws RuntimeException;
}
