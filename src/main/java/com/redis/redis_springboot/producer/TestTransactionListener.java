package com.redis.redis_springboot.producer;


import com.alibaba.fastjson.JSONObject;
import com.redis.redis_springboot.constant.RMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.Map;

/**
 * 生产者监听器（消息事务）
 */
@Slf4j
@RocketMQTransactionListener//(rocketMQTemplateBeanName = "testGroupTransactionMQProducerConfign")//首字母小写
public class TestTransactionListener implements RocketMQLocalTransactionListener {


    /**
     * 执行本地事务
     * @param message
     * @param o
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        MessageHeaders headers = message.getHeaders();
        log.info("事务消息Headers为:{}", headers);
        String rocketmqTags = String.valueOf( headers.get("rocketmq_TAGS"));

        if(headers != null && RMQConstant.Tags.TAG_3.equals(rocketmqTags)){
            String payload = new String((byte[]) message.getPayload());
            log.info("事务消息为:{}", payload);

            Map map = JSONObject.parseObject(payload, Map.class);
            if(map!=null){
                log.info("成功:{}", map);
                return RocketMQLocalTransactionState.COMMIT;
            }

            try {
            }catch (Exception e){
                return RocketMQLocalTransactionState.UNKNOWN;
            }
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        return RocketMQLocalTransactionState.UNKNOWN;

    }

    /**
     * 校验本地事务
     * @param message
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {

        String id = message.getHeaders().get("rocketmq_KEYS").toString();
        log.info("事务消息key为:{}", id);
        if (id!=null) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;


    }
}
