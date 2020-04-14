package com.liu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.liu.constant.RedisConstant;
import com.liu.dao.OrderDao;
import com.liu.dao.SetMealDao;
import com.liu.entity.PageResult;
import com.liu.pojo.Setmeal;
import com.liu.service.OrderService;
import com.liu.service.SetMealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private SetMealDao setMealDao;
    @Autowired
//    @Resource(name = "jedisPool")
    private JedisPool jedisPool;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    //静态页面输出目录
    @Value("${out_put_path}")
    private String outPutPath;
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setMealDao.add(setmeal);
        Integer setmealId = setmeal.getId();//获取增加后的主键
        setSetMealAndCheckGroupRelation(setmealId, checkgroupIds);
        //添加完成后,将图片名保存到Redis集合
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, fileName);
        //添加套餐后,通过freemarker生成静态页面
        generateMobileStaticHtml();
    }
    public void generateMobileStaticHtml(){
        //生成页面前进行查询
        List<Setmeal> setmealList = setMealDao.findAll();
        //需要生成套餐列表页面
        generateMobileSetmealListHtml(setmealList);
        //生成详情页面
        generateMobileSetMealDetailHtml(setmealList);
    }

    /**
     * 生成套餐列表页面
     * @param list
     */
    public void generateMobileSetmealListHtml(List<Setmeal> list){
        HashMap hashMap = new HashMap();
        //为模板提供数据,用于生成静态页面
        hashMap.put("setmealList",list);
        generateHtml("mobile_setmeal.ftl","m_setmeal.html",hashMap);
    }

    /**
     * 生成详情页面,可能有多个
     * @param list
     */
    public void generateMobileSetMealDetailHtml(List<Setmeal>list){
        for (Setmeal setmeal : list) {
            HashMap hashMap = new HashMap();
            hashMap.put("setmeal",setMealDao.findById(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl","setmeal_detail_"+setmeal.getId()+".html",hashMap);
        }

    }
    /**
     * freeMarker生成静态页面(通用方法)
     * @param templateName 要使用的模板
     * @param htmlName 生成后html文件名
     * @param map 生成时所需要的数据
     */
    public void generateHtml(String templateName,String htmlName,Map map) {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out=null;
        try {
            Template template = configuration.getTemplate(templateName);
            out=new FileWriter(new File(outPutPath+"/"+htmlName));
            template.process(map,out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> setmealPage=setMealDao.findByCondition(queryString);
        return new PageResult(setmealPage.getTotal(),setmealPage.getResult());
    }

    @Override
    public List<Setmeal> findAll() {
        return setMealDao.findAll();
    }

    @Override
    public Setmeal findById(Integer id) {
        return setMealDao.findById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        List<Map<String, Object>> mapList= setMealDao.findSetmealCount();
        return mapList;
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
