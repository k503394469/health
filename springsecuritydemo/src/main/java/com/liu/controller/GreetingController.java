package com.liu.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class GreetingController {
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('add')")//必须具有add的权限
    public String add(){
        System.out.println("add......");
        return "success";
    }
    @RequestMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")//必须具有ADMIN角色
    public String delete(){
        System.out.println("delete......");
        return "success";
    }
}
