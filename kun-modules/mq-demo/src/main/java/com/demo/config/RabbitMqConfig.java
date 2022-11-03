package com.demo.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitmq配置类
 *
 * @author gzc
 * @since 2022/11/3 19:38
 **/
@Configuration
public class RabbitMqConfig {
    /**
     * 初始化短信队列
     *
     * @return
     */
    @Bean
    public Queue simpleSmsQueueInit() {
        // 第一个参数是队列名 第二个是持久化
        return new Queue("sms_simple_queue", true);
    }

}
