package com.redis.redis_springboot.consumer;


import com.redis.redis_springboot.constant.RMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = RMQConstant.Topics.RLT_TEST_TOPIC, consumerGroup = RMQConstant.Groups.TEST_GROUP)
public class TransactionMessageListener implements RocketMQListener<String> {


    @Override
    public void onMessage(String s) {
        log.info("接收到事务消息:{}", s);
    }
}
