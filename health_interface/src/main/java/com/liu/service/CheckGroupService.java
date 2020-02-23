package com.liu.service;

import com.liu.entity.PageResult;
import com.liu.entity.QueryPageBean;
import com.liu.pojo.CheckGroup;

public interface CheckGroupService {
    public void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult findPage(QueryPageBean queryPageBean);
}
