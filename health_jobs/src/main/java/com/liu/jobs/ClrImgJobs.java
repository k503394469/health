package com.liu.jobs;

import com.liu.constant.RedisConstant;
import com.liu.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClrImgJobs {
    @Autowired
    private JedisPool jedisPool;
    public void clrImg(){
        //根据两个Redis差值,进行查找要删除的图片
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if (sdiff!=null){
            for (String delImgName : sdiff) {
                //删除服务器上的垃圾图片数据
                QiniuUtils.deleteFileFromQiniu(delImgName);
                //从Redis集合中删除
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,delImgName);
            }
        }
    }
}
