package com.aprilhealth.demo;
/*
 *@author April0ne
 *@date 2019/11/20 17:06
 */


import java.util.Date;

public class B01 {
    private Date orderDate;//预约设置日期
    private Integer number;//可预约人数
    private String message;

    public B01(){}

    public B01(Date orderDate, Integer number, String message) {
        this.orderDate = orderDate;
        this.number = number;
        this.message = message;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "B01{" +
                "orderDate=" + orderDate +
                ", number=" + number +
                ", message='" + message + '\'' +
                '}';
    }
}
