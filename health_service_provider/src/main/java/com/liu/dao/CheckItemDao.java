package com.liu.dao;

import com.github.pagehelper.Page;
import com.liu.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    public void add(CheckItem checkItem);
    //使用PageHelper时,返回值是Page
    public Page<CheckItem> selectByCondition(String queryString);
    public Long findCountById(Integer id);
    public void deleteById(Integer id);
    void editById(CheckItem checkItem);
    CheckItem findById(Integer id);

    List<CheckItem> findAll();
}
