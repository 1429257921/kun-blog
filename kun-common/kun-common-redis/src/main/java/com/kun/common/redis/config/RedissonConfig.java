package com.kun.common.redis.config;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson配置类
 *
 * @author gzc
 * @since 2022/9/21 5:20
 **/
@RequiredArgsConstructor
@Configuration
public class RedissonConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;

    /**
     * RedissonClient,单机模式
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);
        return Redisson.create(config);
    }

//    @Bean
//    public RedissonLocker redissonLocker(RedissonClient redissonClient) {
//        RedissonLocker locker = new RedissonLocker(redissonClient);
//        //设置LockUtil的锁处理对象
//        redissonService.locker = locker;
//        return locker;
//    }
}
