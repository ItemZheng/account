package com.se.account.controller;

import com.se.account.annotation.LoginIgnore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/fe")
public class PageController {

    @LoginIgnore
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/main")
    public String main(){
        return "main";
    }

    @RequestMapping("/create")
    public String create(){
        return "create";
    }
}
