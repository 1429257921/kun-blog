package com.kun.blog.common.log.service;


import com.kun.blog.common.log.entity.LogDTO;

/**
 * 日志业务(需要将接口请求响应信息入库则需要自行实现此接口)
 *
 * @author gzc
 * @since 2022/9/30 20:39
 */
public interface ComLogService {

    /**
     * 日志入库(入库失败会抛出异常)
     *
     * @param logDTO 通用日志DTO对象
     * @author: gzc
     */
    void insertDB(LogDTO logDTO);
}
