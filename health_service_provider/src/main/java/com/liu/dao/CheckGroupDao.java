package com.liu.dao;

import com.github.pagehelper.Page;
import com.liu.pojo.CheckGroup;

import java.util.Map;

public interface CheckGroupDao {
    void add(CheckGroup checkGroup);
    void setCheckGroupAndCheckItem(Map map);
    Page<CheckGroup> findByCondition(String queryString);
}
