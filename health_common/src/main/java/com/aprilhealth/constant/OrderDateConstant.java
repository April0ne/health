package com.aprilhealth.constant;
/*
 *@author April0ne
 *@date 2019/11/19 22:29
 */


public class OrderDateConstant {
    public static final Integer ALLOWED_RESERVATION_TIME_MAXIMUM = 30;//最大可预约日期(服务器时间往后推30天.
    public static final Integer HISTORICAL_TIME_NOT_ALLOW_UPDATES = 0;//批量设置预约信息,历史日期,不可更新预约信息;
    public static final Integer ALLOW_UPDATES = 1;//批量设置预约信息,可更新预约信息;
    public static final Integer NOT_ALLOW_UPDATES = 2;//批量设置预约信息,不可更新预约信息;
    public static final Integer HISTORICAL_TIME_NOT_ALLOW_ADD = 3;//批量设置预约信息,历史日期,不可新增预约信息;
    public static final Integer ALLOW_ADD = 4;//批量设置预约信息,可新增预约信息;
    public static final String  REDIS_KEY_SAVE_EXCEL = "OrderSettingFailed";//将OrderSettingFailed存储至redis(批量上传失败的数据key值)


}
