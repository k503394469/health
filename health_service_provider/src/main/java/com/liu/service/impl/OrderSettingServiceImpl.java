package com.liu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.liu.dao.OrderSettingDao;
import com.liu.pojo.OrderSetting;
import com.liu.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    /**
     * 批量导入OrderSetting到数据库
     * @param orderSettingList
     */
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        if (orderSettingList!=null&&orderSettingList.size()>0){
            for (OrderSetting orderSetting : orderSettingList) {
                //查找数据库当天是否已经添加数据
                int countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());

                if (countByOrderDate>0){
                    //如果添加,就执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    //如果数据库没有当天数据,则进行添加
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String date) {
        String begin=date+"-1";
        String end=date+"-31";
        Map<String,String>map=new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);
        //根据日期范围查询预约设置信息
        List<OrderSetting> list=orderSettingDao.getOrderSettingByMonth(map);
        List<Map<String,Integer>> dataList=new ArrayList<>();
        if (list!=null && list.size()>0){
            for (OrderSetting orderSetting : list) {
                int number = orderSetting.getNumber();
                Date orderDate = orderSetting.getOrderDate();
                int reservations = orderSetting.getReservations();
                int day = orderDate.getDate();
                HashMap<String, Integer> dataMap = new HashMap<>();
                dataMap.put("date",day);
                dataMap.put("number",number);
                dataMap.put("reservations",reservations);
                dataList.add(dataMap);
            }
        }
        return dataList;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //根据日期判断是否已经有数据
        Date orderDate = orderSetting.getOrderDate();
        int countByOrderDate = orderSettingDao.findCountByOrderDate(orderDate);
        if (countByOrderDate>0){
            //当前日期已经有数据
            //执行更新操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            //当前日期没有数据
            //执行插入
            orderSettingDao.add(orderSetting);
        }
    }
}
