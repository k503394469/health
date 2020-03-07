package com.liu.dao;

import com.liu.pojo.Setmeal;

import java.util.Map;

public interface SetMealDao {
    void add(Setmeal setmeal);
    void setSetMealAndCheckGroup(Map<String,Integer>idMap);
}
