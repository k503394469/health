package com.liu.service;

import com.liu.entity.PageResult;
import com.liu.pojo.Setmeal;

import java.util.List;

public interface SetMealService {
    void add(Setmeal setmeal, Integer []checkgroupIds);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);
}
