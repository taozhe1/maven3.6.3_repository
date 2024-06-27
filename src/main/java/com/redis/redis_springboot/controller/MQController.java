package com.redis.redis_springboot.controller;


import com.redis.redis_springboot.producer.RocketMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/mq")
@RestController
public class MQController {


    @Autowired
    private RocketMQProducer mqProducer;

    @RequestMapping("/send")
    public void send(){
        mqProducer.testProducter();
        System.out.println(111);
    }


}
