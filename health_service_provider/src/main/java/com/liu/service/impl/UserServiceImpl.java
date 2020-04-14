package com.liu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.liu.dao.PermissionDao;
import com.liu.dao.RoleDao;
import com.liu.dao.UserDao;
import com.liu.pojo.Permission;
import com.liu.pojo.Role;
import com.liu.pojo.User;
import com.liu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    //根据用户名查询数据库,同时查询角色和关联的权限
    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);//用户基本信息,不包含角色
        if (user==null){
            return null;
        }
        Integer id = user.getId();
        //根据用户查询对应的角色
        Set<Role> roleSet = roleDao.findByUserId(id);
        for (Role role : roleSet) {
            //根据角色id查询权限
            Integer roleId = role.getId();
            Set<Permission> permissionSet = permissionDao.findByRoleId(roleId);
            role.setPermissions(permissionSet);//角色关联权限
        }
        user.setRoles(roleSet);//用户关联角色
        return user;
    }
}
