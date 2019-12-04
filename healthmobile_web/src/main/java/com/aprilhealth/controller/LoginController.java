package com.aprilhealth.controller;
/*
 *@author April0ne
 *@date 2019/11/23 15:00
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.aprilhealth.constant.MessageConstant;
import com.aprilhealth.constant.RedisMessageConstant;
import com.aprilhealth.entity.Result;
import com.aprilhealth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Reference
    private LoginService loginService;

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/check")
    public Result method(HttpServletResponse response, @RequestBody Map map) throws Exception{
        Result result = null;
        //判断验证码是否正确
        String telephone = (String)map.get("telephone");
        if (jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN) == null) {
            //不正确
            return result = new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        try {
            //校验手机号
            boolean isLogin = loginService.checkTelephone(telephone);
            result = new Result(true,MessageConstant.LOGIN_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(false,MessageConstant.LOGIN_FAIL);
        }

        //记住手机号
        Cookie cookie = new Cookie("login_member_tel",telephone);
        //设置共享cookie路径
        cookie.setPath("/");
        //设置有效期
        cookie.setMaxAge(60*60*24*30);
        response.addCookie(cookie);
        return result;
    }
}
