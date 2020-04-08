package com.liu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class SpringSecurityUserService2 implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    //根据用户名查询用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //username 来源于页面输入
        System.out.println("用户名是:"+username);
        //根据用户名查询用户信息,包含密码信息
        //返回用户信息给框架,框架进行密码比对
        //为用户授权
        //...
        List<GrantedAuthority> authorityList=new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("permission_A"));//权限
        authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));//授予角色
        //使用框架USER
        User user = new User(username,bCryptPasswordEncoder.encode("xxx"),authorityList);//使用BCrypt加密
        System.out.println(user.getPassword());
        return user;
    }


}
