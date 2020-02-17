package com.liu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.liu.dao.CheckItemDao;
import com.liu.entity.PageResult;
import com.liu.entity.QueryPageBean;
import com.liu.pojo.CheckItem;
import com.liu.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项service实现类
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        System.out.println(queryString);
        //使用pageHelper
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> items = checkItemDao.selectByCondition(queryString);
        long total = items.getTotal();
        List<CheckItem> rows = items.getResult();
        return new PageResult(total,rows);
    }

    @Override
    public void deleteById(Integer id) {
        Long itemCount = checkItemDao.findCountById(id);
        if (itemCount>0){
            new RuntimeException();
        }
        checkItemDao.deleteById(id);
    }
}
