package com.demo.controller;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author gzc
 * @since 2022/11/3 19:40
 **/
@RestController
public class TestController {

    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("test1")
    public String test1() {
        // 生成测试字符串
        String code = UUID.randomUUID().toString();
        // 向sms_simple_queue这个队列发送code字符串
        rabbitTemplate.convertAndSend("sms_simple_queue", code);
        return "OK";
    }

    @GetMapping("test2")
    public String test2() {
        // 生成测试字符串
        String code = UUID.randomUUID().toString();
        rocketMQTemplate.convertAndSend("first-topic", code);
        return "OK";
    }
}
