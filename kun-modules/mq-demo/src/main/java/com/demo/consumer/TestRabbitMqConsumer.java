package com.demo.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * rabbitmq消费者
 *
 * @author gzc
 * @since 2022/11/3 19:49
 **/
@Component
public class TestRabbitMqConsumer {
    /**
     * 简单模式
     *
     * @param message
     */
    @RabbitListener(queues = {"sms_simple_queue"})
    public void getSmsConsumer(String message) {
        System.out.println("rabbbitmq短信消费者接收到信息:" + message);
    }

}
