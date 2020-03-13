package com.liu.dao;

import com.liu.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    void add(OrderSetting orderSetting);
    void editNumberByOrderDate(OrderSetting orderSetting);
    int findCountByOrderDate(Date date);

    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);
}
