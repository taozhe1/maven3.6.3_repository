package com.redis.redis_springboot.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class PageController {


    @RequestMapping("/page")
    public String getPage(Model model) {
        // 处理Thymeleaf页面请求，返回Thymeleaf模板名称
        // 可以在model中设置数据供Thymeleaf使用
        model.addAttribute("message", "Hello from Controller!");
        return "myPage";
    }

}
