package com.liu.controller;

import com.liu.constant.MessageConstant;
import com.liu.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    //获得登录用户名
    @RequestMapping("/getUsername")
    public Result getUsername() {
        //当SpringSecurity完成认证时,会将当前用户信息,保存到上下文中
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();//提供框架的User对象
        if (user!=null){
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        }else {
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
