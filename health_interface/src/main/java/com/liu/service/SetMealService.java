package com.liu.service;

import com.liu.entity.PageResult;
import com.liu.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealService {
    void add(Setmeal setmeal, Integer []checkgroupIds);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Map<String, Object>> findSetmealCount();

}
