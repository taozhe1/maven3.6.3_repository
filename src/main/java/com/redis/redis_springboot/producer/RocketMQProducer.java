package com.redis.redis_springboot.producer;


import com.redis.redis_springboot.rocketmq.RocketMqHelper;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class RocketMQProducer {


    @Autowired
    private RocketMqHelper rocketMqHelper;


    public void testProducter(){


         Map<Object, Object> hashMap = new HashMap<>();
         hashMap.put("a","a");
        hashMap.put("b","b");
        hashMap.put("c","c");
        hashMap.put("d","d");

        SendResult rltTestTopic = rocketMqHelper.sendMsg("RLT_TEST_TOPIC", hashMap);
        System.out.println("rltTestTopic=="+rltTestTopic);
    }



}
