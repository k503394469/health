package com.liu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.liu.dao.OrderSettingDao;
import com.liu.pojo.OrderSetting;
import com.liu.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
