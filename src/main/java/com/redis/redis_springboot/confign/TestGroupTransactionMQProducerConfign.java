/*
package com.redis.redis_springboot.confign;


import org.apache.rocketmq.spring.annotation.ExtRocketMQTemplateConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;


*/
/**
 * 首先我们需要配置自定义的 RocketMQTemplate，最重要的是设置 TransactionMQProducer 的生产者组名称
 *
 * @ExtRocketMQTemplateConfiguration：用于标识一个类是用于配置扩展的 RocketMQTemplate。通过在这个类上使用 @ExtRocketMQTemplateConfiguration 注解，可以实现自定义的 RocketMQTemplate 配置。
 *
 * group: 这是 @ExtRocketMQTemplateConfiguration 注解的一个参数，用于指定 RocketMQ 生产者组的名称
 *//*

@ExtRocketMQTemplateConfiguration(group = "test-group")
public class TestGroupTransactionMQProducerConfign extends RocketMQTemplate {
}
*/
