package com.demo.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
    public void getSmsConsumer(Message message, Channel channel) throws IOException {
        System.out.println("rabbbitmq短信消费者接收到信息:" + new String(message.getBody()));
        // 确认消息
        // 第一个参数，交付标签，相当于消息ID 64位的长整数(从1开始递增)
        // 第二个参数，false表示仅确认提供的交付标签；true表示批量确认所有消息(消息ID小于自身的ID)，包括提供的交付标签
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
