package com.aprilhealth.utils;
/*
 *@author April0ne
 *@date 2019/11/22 21:46
 */


import com.aprilhealth.constant.OrderDateConstant;

import java.util.Calendar;
import java.util.Date;

public class ReservationTimeUtils {
    /**
     * 判断当前时间是否可预约检查
     * @return
     */
    public static boolean isReservation(Date orderDate)throws RuntimeException{
        //可预约的最大时间
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, OrderDateConstant.ALLOWED_RESERVATION_TIME_MAXIMUM);
        Date reservationMaxTime = cal.getTime();
        return reservationMaxTime.after(orderDate);
    }
}
