package com.liu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.liu.constant.RedisConstant;
import com.liu.dao.SetMealDao;
import com.liu.entity.PageResult;
import com.liu.pojo.Setmeal;
import com.liu.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealDao setMealDao;
    @Autowired
//    @Resource(name = "jedisPool")
    private JedisPool jedisPool;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setMealDao.add(setmeal);
        Integer setmealId = setmeal.getId();//获取增加后的主键
        setSetMealAndCheckGroupRelation(setmealId, checkgroupIds);
        //添加完成后,将图片名保存到Redis集合
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, fileName);
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> setmealPage=setMealDao.findByCondition(queryString);
        return new PageResult(setmealPage.getTotal(),setmealPage.getResult());
    }

    //增加setmeal和checkgroup表关联
    public void setSetMealAndCheckGroupRelation(Integer setmealId, Integer[] checkgroupIds) {
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (Integer checkgroupId : checkgroupIds) {
                Map<String, Integer> idMap = new HashMap<>();
                idMap.put("setmealId", setmealId);
                idMap.put("checkgroupId", checkgroupId);
                setMealDao.setSetMealAndCheckGroup(idMap);
            }
        }
    }
}
