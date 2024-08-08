package com.redis.redis_springboot.producer;


import com.alibaba.fastjson.JSONObject;
import com.redis.redis_springboot.constant.RMQConstant;
import com.redis.redis_springboot.util.RocketMqHelperUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class RocketMQProducer {


    @Autowired
    private RocketMqHelperUtils rocketMqHelperUtils;


    public void testProducter(){

        Map<Object, Object> hashMap = new HashMap<>();
        hashMap.put("a","a");
        hashMap.put("b","b");
        hashMap.put("c","c");
        hashMap.put("d","d");

        SendResult rltTestTopic = rocketMqHelperUtils.sendMsg(RMQConstant.Topics.RLT_TEST_TOPIC,RMQConstant.Tags.TAG_1, hashMap);
        System.out.println("rltTestTopic=="+rltTestTopic);
    }

    public void testProducter2(){


        Map<Object, Object> hashMap = new HashMap<>();
        hashMap.put("a","a");
        hashMap.put("b","b");
        hashMap.put("c","c");
        hashMap.put("d","d");

        rocketMqHelperUtils.asyncSend(RMQConstant.Topics.RLT_TEST_TOPIC,RMQConstant.Tags.TAG_2 ,hashMap);

    }

    public void testProducter3(){


        Map<Object, Object> hashMap = new HashMap<>();
        hashMap.put("a","a");
        hashMap.put("b","b");
        hashMap.put("c","c");
        hashMap.put("d","d");

        String s = JSONObject.toJSONString(hashMap);

        rocketMqHelperUtils.sendMessageInTransaction(RMQConstant.Topics.RLT_TEST_TOPIC,RMQConstant.Tags.TAG_3, s);

    }



}
