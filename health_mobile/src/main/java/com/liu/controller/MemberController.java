package com.liu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.liu.constant.MessageConstant;
import com.liu.constant.RedisMessageConstant;
import com.liu.entity.Result;
import com.liu.pojo.Member;
import com.liu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Map;

/**
 * 处理会员相关
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Reference
    private MemberService memberService;
    @Autowired
    private JedisPool jedisPool;
    public Result login(@RequestBody Map map){
        String telephone = (String) map.get("telephone");
        String validateCodeHtml = (String) map.get("validateCode");
        //Redis中取出保存的验证码
        String validateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (validateCode!=null&&validateCodeHtml!=null&&validateCode.equals(validateCodeHtml)){
            //验证码正确
            //判断当前用户是否是会员
            Member member = memberService.findByTelephone(telephone);
            if (member==null){
                member=new Member();
                //自动完成注册,保存到member表
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberService.add(member);
                /**
                 *
                 * 到
                 * 这
                 * 里
                 * 了
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 */
            }
        }else {
            //错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        return null;
    }
}
