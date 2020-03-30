package com.liu.controller;

import com.aliyuncs.exceptions.ClientException;
import com.liu.constant.MessageConstant;
import com.liu.constant.RedisMessageConstant;
import com.liu.entity.Result;
import com.liu.utils.SMSUtils;
import com.liu.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 验证码操作
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;
    /**
     * 体检预约,发送验证码
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        //随机生成4或者6位验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        //发送
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //验证码保存到Redis 5分钟
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,5*60,validateCode.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
