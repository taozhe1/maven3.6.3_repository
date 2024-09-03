package com.redis.redis_springboot.consumer;


import com.redis.redis_springboot.constant.RMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(
        topic = RMQConstant.Topics.RLT_TEST_TOPIC,
        consumerGroup = RMQConstant.Groups.TEST_GROUP,
        selectorExpression = RMQConstant.Tags.TAG_3)
public class TransactionMessageListener implements RocketMQListener<MessageExt> {


    @Override
    public void onMessage(MessageExt message) {
        log.info("监听到消息：message:{}", message);
        String tags = message.getTags();
        byte[] body = message.getBody();
        String s = new String(body);

        if(RMQConstant.Tags.TAG_3.equals(tags)){
            log.info(tags+"消息为："+s);
        }
    }
}
