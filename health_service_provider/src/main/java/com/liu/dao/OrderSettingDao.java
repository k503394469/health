package com.liu.dao;

import com.liu.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    void add(OrderSetting orderSetting);
    void editNumberByOrderDate(OrderSetting orderSetting);
    int findCountByOrderDate(Date date);
    //更新已预约人数
    public void editReservationsByOrderDate(OrderSetting orderSetting);
    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    OrderSetting findByOrderDate(Date parseString2Date);
}
