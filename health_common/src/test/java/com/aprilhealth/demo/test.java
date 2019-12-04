package com.aprilhealth.demo;
/*
 *@author April0ne
 *@date 2019/11/22 17:31
 */


import com.aprilhealth.utils.SMSUtils;
import com.aprilhealth.utils.ValidateCodeUtils;

public class test {
    public static void main(String[] args) throws Exception {
        SMSUtils.sendShortMessage("SMS_178450771","18188939049", String.valueOf(ValidateCodeUtils.generateValidateCode(6)));
    }
}
