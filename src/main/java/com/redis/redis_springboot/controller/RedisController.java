package com.redis.redis_springboot.controller;

import com.alibaba.druid.util.StringUtils;
import com.redis.redis_springboot.bean.TProv;
import com.redis.redis_springboot.service.TProvService;
import com.redis.redis_springboot.service.YcfVpnService;
import com.redis.redis_springboot.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/devRedis")
public class RedisController {

    /*
    注入
     */
    @Autowired
    private RedisTemplate redisTemplate;
     @Autowired
    private RedisUtil redisUtil;

     @Autowired
    private YcfVpnService ycfVpnService;

    @Autowired
    TProvService tProvService;

    @RequestMapping("/map")
    public Map mapTest(@RequestBody Map map){
        if(map!=null && map.size()>0){
          return map;

        }


        return null;

    }

     @RequestMapping("/redis")
    public String TestRedis(){
        //设置值到redis中
        redisTemplate.opsForValue().set("name","taozhe");
        //从redis中拿出值
        Object name = redisTemplate.opsForValue().get("name");
        return (String) name;
    }

    @RequestMapping("/redis2")
    public long TestRedis2(){
        //boolean name = redisUtil.expire("name", 20);
      //  long name1 = redisUtil.getExpire("name");
        //System.out.println(name+"===时间（秒）");
      //  System.out.println(name1+"===");
        return 1;
    }

     @RequestMapping("/redis3")
    public String getRedis(){
        String  a =null;
        try {
            //从redis中拿出值
            String name = (String)redisTemplate.opsForValue().get("addItemNotExists_13035637305_13035637305");
            System.out.println(redisUtil.getExpire("addItemNotExists_13035637305_13035637305"));
            return  name;
        }catch (Exception e){
             a = e.toString();
        }

    return a;
    }

    @RequestMapping("/dev")
    public String dev(){
       return  "成功";
    }

    @RequestMapping("/getBean")
    public Integer getBean(){
        try {
            return ycfVpnService.listCount();

        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    @RequestMapping("/getBean2")
    public TProv getBean2(){
        System.out.println("日志进来,次数");
        try {
            return tProvService.getById("10");

        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }


public static int i = 0;
    @RequestMapping("/addItemNotExists")
    public String addItemNotExists(@RequestParam String id, @RequestParam String phone, @RequestParam long time) {
        try {
            String key = "addItemNotExists_" + id + "_" + phone;
            if(!StringUtils.isEmpty((String) redisUtil.get(key))){
                throw new Exception("已存在，请稍后获取");
            }
            redisUtil.set(key, phone, time);

            System.out.println(redisUtil.get("addItemNotExists_" +id + "_" + phone));
            System.out.println("方法请求成功次数=="+i);

        }catch (Exception e){
            System.out.println("方法请求失败次数=="+i);
            System.out.println(e);

        }

        i++;
        System.out.println("方法请求次数=="+i);
        return "成功";

    }






}
