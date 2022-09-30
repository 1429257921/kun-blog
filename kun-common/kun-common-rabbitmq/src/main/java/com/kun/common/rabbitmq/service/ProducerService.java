package com.kun.common.rabbitmq.service;

/**
 * 生产者统一接口
 *
 * @author gzc
 * @since 2022/9/30 20:40
 */
public interface ProducerService {

    /**
     * 发送消息
     *
     * @param message
     */
    void sendMessage(String message);
}
