package com.liu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.liu.constant.MessageConstant;
import com.liu.constant.RedisMessageConstant;
import com.liu.entity.Result;
import com.liu.pojo.Order;
import com.liu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        String telephone = (String) map.get("telephone");
        String validateCodeHtml = (String) map.get("validateCode");
        //redis中获取验证码,进行比对
        String validateCodeRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        if (validateCodeRedis!=null&&validateCodeHtml!=null&&validateCodeHtml.equals(validateCodeRedis)){
            //在线预约或者电话预约
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            //比对成功
            Result order=null;
            try {
                order = orderService.order(map);
            }catch (Exception e){
                e.printStackTrace();
                return order;
            }
            if (order.isFlag()){
                //发送预约成功短信
            }
            return order;
        }else {
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
    }
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map orderMap=orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,orderMap);
        }catch (Exception e){
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
