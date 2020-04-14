package com.liu.dao;

import com.liu.pojo.Role;

import java.util.List;
import java.util.Set;

public interface RoleDao {
    Set<Role> findByUserId(Integer id);
}
