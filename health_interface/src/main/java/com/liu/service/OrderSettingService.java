package com.liu.service;

import com.liu.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void add(List<OrderSetting> orderSettingList);

    List<Map<String, Integer>> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);
}
