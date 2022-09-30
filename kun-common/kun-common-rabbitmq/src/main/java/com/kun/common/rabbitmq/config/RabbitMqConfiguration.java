package com.kun.common.rabbitmq.config;

import com.kun.common.core.factory.YamlPropertySourceFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 添加rabbitmq相关配置
 *
 * @author gzc
 * @since 2022/9/22 2:43
 **/
@Configuration
@PropertySource(factory = YamlPropertySourceFactory.class,value = "classpath:kun-rabbitmq.yml")
public class RabbitMqConfiguration {
}
