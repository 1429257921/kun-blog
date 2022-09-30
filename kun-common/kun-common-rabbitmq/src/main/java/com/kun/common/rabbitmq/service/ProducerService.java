package com.kun.common.rabbitmq.service;

/**
 * 生产者接口
 *
 * @author gzc
 * @since 2022/9/22 2:54
 **/
public interface ProducerService {

    /**
     * 发送消息
     *
     * @param message
     */
    void sendMessage(String message);
}
