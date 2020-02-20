package com.liu.service;

import com.liu.entity.PageResult;
import com.liu.entity.QueryPageBean;
import com.liu.pojo.CheckItem;

import java.util.List;

//检查项(CheckItem)接口
public interface CheckItemService {
    public void add(CheckItem checkItem);
    public PageResult pageQuery(QueryPageBean queryPageBean);
    void deleteById(Integer id);

    void edit(CheckItem checkItem);

    CheckItem findById(Integer id);

    List<CheckItem> findAll();

}
