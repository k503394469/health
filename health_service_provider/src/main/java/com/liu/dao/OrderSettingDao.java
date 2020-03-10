package com.liu.dao;

import com.liu.pojo.OrderSetting;

import java.util.Date;

public interface OrderSettingDao {
    void add(OrderSetting orderSetting);
    void editNumberByOrderDate(OrderSetting orderSetting);
    int findCountByOrderDate(Date date);
}
