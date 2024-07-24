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
    public String send(){

        long start = System.currentTimeMillis();
        for (int i = 0; i <5000; i++) {
            mqProducer.testProducter();
        }
        long end = System.currentTimeMillis();
        return "耗时=="+(end - start);
    }

    @RequestMapping("/send2")
    public String send2(){

        long start = System.currentTimeMillis();
        for (int i = 0; i <5000; i++) {
            mqProducer.testProducter2();
        }
        long end = System.currentTimeMillis();

        return "耗时=="+(end - start);
    }

    @RequestMapping("/send3")
    public String send3(){

        long start = System.currentTimeMillis();
        for (int i = 0; i <5000; i++) {
            mqProducer.testProducter3();
            System.out.println("循环次数"+i);
        }
        long end = System.currentTimeMillis();

        return "耗时=="+(end - start);
    }


}
