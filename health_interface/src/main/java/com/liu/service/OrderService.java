package com.liu.service;

import com.liu.entity.Result;

import java.util.Map;

public interface OrderService {
    public Result order(Map map) throws Exception;

    Map findById(Integer orderId) throws Exception;
}
