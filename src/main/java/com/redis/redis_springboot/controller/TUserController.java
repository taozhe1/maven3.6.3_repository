package com.redis.redis_springboot.controller;


import com.redis.redis_springboot.bean.TUser;
import com.redis.redis_springboot.service.TUserService;
import com.redis.redis_springboot.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/user")
public class TUserController {

    @Autowired
    private TUserService tUserService;
    @Autowired
    SecurityUtils securityUtils;

    @Autowired
    TUserController tUserController;

    @RequestMapping("/list")
    public String list(Model model){

        System.out.println("12313123");

        //System.out.println("3333333331");

        model.addAttribute("users",tUserService.list());
        return "userList";

    }

    @RequestMapping("/save")
    @ResponseBody
    public boolean save(@RequestBody TUser tUser){
        tUser.setPassword(securityUtils.encodePassword(tUser.getPassword()));
        return tUserService.save(tUser);
    }

    @RequestMapping("/getById")
    @ResponseBody
    public TUser getById(@RequestParam int id,int is,int isex) throws Exception{
        TUser byId = tUserService.getById(id);

        if(is==0){
            this.del(id,isex);
        }else if(is==2){
            del(id,isex);
        }else if(is==3){
            tUserController.del(id,isex);
        }
        return byId;
    }

    @RequestMapping("/del")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public boolean del(@RequestParam int id,int isex){
        tUserService.removeById(id);
        if(isex==0){
            int i = 1 / 0;
        }
        return true;
    }

    @RequestMapping("/update")
    @ResponseBody
    public boolean update(@RequestBody TUser tUser){

        return tUserService.updateById(tUser);

    }



    @RequestMapping("/getByName")
    @ResponseBody
    public List<TUser> getByName(@RequestParam String userName){
        return tUserService.getByName(userName);
    }

    @RequestMapping("/login")
    public String loginHtml() {
        return "login"; // 不要加上.html后缀
    }

    @RequestMapping("/unauth")
    public String unauth() {
        return "unauth"; // 不要加上.html后缀
    }

    @RequestMapping("/logout")
    public String performLogout() {
        // 重定向到注销成功页面或任何其他页面
        return "redirect:/login";
    }


}
