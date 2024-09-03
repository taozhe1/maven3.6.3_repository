package com.redis.redis_springboot.controller;


import com.redis.redis_springboot.annotation.ElapsedTimeAnnotation;
import com.redis.redis_springboot.bean.TdGoods;
import com.redis.redis_springboot.producer.RocketMQProducer;
import com.redis.redis_springboot.service.TdGoodsService;
import com.redis.redis_springboot.util.FrontResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RequestMapping("/mq")
@RestController
public class MQController {


    @Autowired
    private RocketMQProducer mqProducer;

    @Autowired
    TdGoodsService goodsService;

    @Autowired
    ResponseController responseController;


    @RequestMapping("/send")
    public String send(){

        long start = System.currentTimeMillis();
        for (int i = 0; i <1000; i++) {
            mqProducer.testProducter();
        }
        long end = System.currentTimeMillis();
        return "耗时=="+(end - start);
    }

    @RequestMapping("/send2")
    public String send2(){

        long start = System.currentTimeMillis();
        for (int i = 0; i <1000; i++) {
            mqProducer.testProducter2();
        }
        long end = System.currentTimeMillis();

        return "耗时=="+(end - start);
    }

    @RequestMapping("/send3")
    public String send3(){

        long start = System.currentTimeMillis();
        for (int i = 0; i <1000; i++) {
            mqProducer.testProducter3();
            System.out.println("循环次数"+i);
        }
        long end = System.currentTimeMillis();

        return "耗时=="+(end - start);
    }

    @RequestMapping("/send4")
    public FrontResult send4(@RequestBody TdGoods tdGoods) throws Exception {

         goodsService.orderById(tdGoods);

        return new FrontResult("0");

    }

    @RequestMapping("/getName")
    @ElapsedTimeAnnotation(arbitrarily = "#tdGoods.phoneNumber")
    public FrontResult getName(@RequestBody TdGoods tdGoods) throws Exception {
        List<TdGoods> byName = goodsService.getByName(tdGoods);
       // @RequestParam 注解的作用是将HTTP请求中的查询参数或表单参数绑定到方法的参数上，但在代码内部直接调用时，这种绑定机制不再适用
        //responseController.exceptionDispose(2);
        return new FrontResult(byName);
    }

    @RequestMapping("/getById")
    public FrontResult getById(@RequestParam List<Long> id) throws Exception {
        List<TdGoods> byName = goodsService.getById(id);
        return new FrontResult(byName);
    }


}
