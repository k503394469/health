package com.liu.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SpringSecurityUserService implements UserDetailsService {
    //根据用户名查询用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //username 来源于页面输入
        System.out.println("用户名是:"+username);
        return null;
    }


}
