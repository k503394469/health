package com.liu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.liu.dao.CheckGroupDao;
import com.liu.entity.PageResult;
import com.liu.entity.QueryPageBean;
import com.liu.pojo.CheckGroup;
import com.liu.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    CheckGroupDao checkGroupDao;

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //插入t_checkgroup表
        checkGroupDao.add(checkGroup);
        //获取上面语句所插入的ID值
        Integer checkGroupId = checkGroup.getId();
        setAssociation(checkGroupId,checkitemIds);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> checkGroupPage=checkGroupDao.findByCondition(queryString);
        return new PageResult(checkGroupPage.getTotal(),checkGroupPage.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //修改检查组
        checkGroupDao.edit(checkGroup);
        //清理检查组关联的检查项(中间关系表)
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //重新建立关系
        Integer checkGroupId = checkGroup.getId();
        setAssociation(checkGroupId,checkitemIds);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    //建立检查组与检查项关系
    public void setAssociation(Integer checkGroupId,Integer[] checkitemIds){
        if (checkitemIds!=null&&checkitemIds.length>0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> idMap=new HashMap<>();
                idMap.put("checkGroupId",checkGroupId);
                idMap.put("checkitemId",checkitemId);
                //插入中间关系表
                checkGroupDao.setCheckGroupAndCheckItem(idMap);
            }
        }
    }
}
