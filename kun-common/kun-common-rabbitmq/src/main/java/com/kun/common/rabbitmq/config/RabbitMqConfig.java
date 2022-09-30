package com.kun.common.rabbitmq.config;

import com.kun.common.core.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.stream.binder.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;

/**
 * rabbitMQ客户端配置
 *
 * @author gzc
 * @since 2022/9/22 4:25
 **/
@RequiredArgsConstructor
@Configuration
public class RabbitMqConfig {
    private final BinderFactory binderFactory;

    private final static String binderName = "default-binder";

    @SuppressWarnings("all")
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) throws NoSuchFieldException, IllegalAccessException {
        // 获取目标binder
        Binder<BinderConfiguration, ? extends ConsumerProperties, ? extends ProducerProperties> binder =
                binderFactory.getBinder(binderName, BinderConfiguration.class);
        Assert.notNull(binder, binderName + " is null");

        // 获取binder的connectionFactory
        Field field = binder.getClass().getDeclaredField("connectionFactory");
        field.setAccessible(true);
        connectionFactory = (ConnectionFactory) field.get(binder);

        // new
        return new RabbitTemplate(connectionFactory);
    }
}
