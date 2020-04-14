package com.liu.dao;

import com.liu.pojo.User;

public interface UserDao {
    User findByUsername(String username);
}
