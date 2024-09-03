package com.redis.redis_springboot.consumer;

import com.alibaba.fastjson.JSONObject;
import com.redis.redis_springboot.bean.TdGoods;
import com.redis.redis_springboot.constant.RMQConstant;
import com.redis.redis_springboot.dao.TdGoodsDao;
import com.redis.redis_springboot.util.RedisUtil;
import com.redis.redis_springboot.util.RocketMqHelperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * topic需要和生产者的topic一致，consumerGroup属性是必须指定的，内容可以随意
 * selectorExpression的意思指的就是tag，默认为“*”，不设置的话会监听所有消息
 */
@Component
@Slf4j
@RocketMQMessageListener(
        consumerGroup = RMQConstant.Groups.TEST_GROUP,
        topic = RMQConstant.Topics.RLT_TEST_TOPIC,
       // selectorExpression = RMQConstant.Tags.TAG_1 +"||"+ RMQConstant.Tags.TAG_2 +"||"+ RMQConstant.Tags.TAG_4)
        selectorExpression = "*")
public class RocketMqConsumer implements RocketMQListener<MessageExt> {


    @Autowired
    private TdGoodsDao goodsDao;
    @Autowired
    private RocketMqHelperUtils rocketMqHelperUtils;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void onMessage(MessageExt message) {
        log.info("监听到消息：message:{}", message);
        String tags = message.getTags();
        byte[] body = message.getBody();

        String s = new String(body);

        if(RMQConstant.Tags.TAG_1.equals(tags)){
            log.info(tags+"消息为："+s);

        }else  if(RMQConstant.Tags.TAG_2.equals(tags)){
            log.info(tags+"消息为："+s);
        }else  if(RMQConstant.Tags.TAG_4.equals(tags)){
            log.info(tags+"消息为："+s);

            TdGoods tdGoods = JSONObject.parseObject(s, TdGoods.class);
            try {
                int i = goodsDao.orderById(tdGoods);

                TdGoods tdGoods1 = goodsDao.selectById(tdGoods.getId());

                log.info(String.format("修改库存结果为[%d]，库存出参结果为[%s]", i,JSONObject.toJSONString(tdGoods1)));

                if(i == 0){

                    throw new Exception("无库存，删减失败");
                }

            }catch (Exception e){

                //if( <=3){
                    rocketMqHelperUtils.asyncSend(RMQConstant.Topics.RLT_TEST_TOPIC,RMQConstant.Tags.TAG_4 ,tdGoods);
                    log.error(tags+"消费信息异常--->",e);
                }
            //}
        }
    }
}