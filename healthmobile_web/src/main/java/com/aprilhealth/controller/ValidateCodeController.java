package com.aprilhealth.controller;
/*
 *@author April0ne
 *@date 2019/11/22 8:46
 *校验验证码
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.aprilhealth.constant.MessageConstant;
import com.aprilhealth.constant.RedisConstant;
import com.aprilhealth.constant.RedisMessageConstant;
import com.aprilhealth.entity.Result;
import com.aprilhealth.pojo.Setmeal;
import com.aprilhealth.service.SetmealService;
import com.aprilhealth.utils.SMSUtils;
import com.aprilhealth.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import java.util.List;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 发送验证码校验:下单
     * @param telephone
     * @return
     * @throws Exception
     */
    @GetMapping("/send4Order/{telephone}")
    public Result send4Order(@PathVariable(value = "telephone") String telephone){
        Result result = null;
        String codeStr = ValidateCodeUtils.generateValidateCode4String(4);
        try {
            SMSUtils.sendShortMessage("SMS_178450771", telephone,codeStr);
            //将数据存储至redis
//           //方式一 缺点,没有设置过期时间,后续还要花费额外的资源处理垃圾数据
//            jedisPool.getResource().sadd(RedisMessageConstant.SENDTYPE_ORDER,codeStr);
            //方式二 : 中间参数表示,该key的有效时间,失效自动销毁.setex = set + expire(到期)
            jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_ORDER,60,codeStr);
            result = new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS,codeStr);
        } catch (ClientException e) {
            e.printStackTrace();
            result = new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        return result;
    }

    /**
     * 发送验证码校验:登录
     * @param telephone
     * @return
     * @throws Exception
     */
    @GetMapping("/send4Login/{telephone}")
    public Result send4Login(@PathVariable(value = "telephone") String telephone){
        Result result = null;
        String codeStr = ValidateCodeUtils.generateValidateCode4String(4);
        try {
            SMSUtils.sendShortMessage("SMS_178450771", telephone,codeStr);
            jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,60,codeStr);
            result = new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS,codeStr);
        } catch (ClientException e) {
            e.printStackTrace();
            result = new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        return result;
    }


}
