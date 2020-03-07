package com.liu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.liu.constant.MessageConstant;
import com.liu.constant.RedisConstant;
import com.liu.entity.Result;
import com.liu.pojo.Setmeal;
import com.liu.service.SetMealService;
import com.liu.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

//套餐控制器
@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    //使用JedisPool操作临时图片保存
    @Qualifier("jedisPool")
    private JedisPool jedisPool;
    //文件上传
    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile) {
        //截取后缀
        String originalFilename = imgFile.getOriginalFilename();
        int indexOfEx = originalFilename.lastIndexOf(".");
        String extension = originalFilename.substring(indexOfEx - 1);
        String fileName= UUID.randomUUID().toString()+extension;
        try {
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            //redis添加临时图片的名称,添加到set集合
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        //fileName是Result里面的Data
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
    }
    @Reference
    private SetMealService setMealService;
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer []checkgroupIds) {
        try {
            setMealService.add(setmeal,checkgroupIds);
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

}
