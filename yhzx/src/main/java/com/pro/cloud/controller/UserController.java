package com.pro.cloud.controller;

import com.pro.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/getUserInfo")
    public String getUserInfo() {
        return "1";
    }

    @PostMapping("/get")
    public Object get(){
        return userService.get("p1","p2");
    }



}
