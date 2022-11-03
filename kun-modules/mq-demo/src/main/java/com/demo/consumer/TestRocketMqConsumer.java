package com.demo.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * rocketmq消费者
 *
 * @author gzc
 * @since 2022/11/3 20:17
 **/
@Component
@RocketMQMessageListener(topic = "first-topic", consumerGroup = "my-consumer-group")
public class TestRocketMqConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println("RocketMq我收到消息了！消息内容为：" + message);
    }
}
