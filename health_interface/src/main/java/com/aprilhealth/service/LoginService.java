package com.aprilhealth.service;
/*
 *@author April0ne
 *@date 2019/11/23 15:00
 *处理登录业务需求
 */


public interface LoginService {

    /**
     * 登录校验
     * @param telephone
     * @return
     */
    boolean checkTelephone(String telephone);

}
