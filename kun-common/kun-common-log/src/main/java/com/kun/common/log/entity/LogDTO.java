package com.kun.common.log.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用日志dto对象
 *
 * @author gzc
 * @since 2022/9/30 20:39
 */
@Data
public class LogDTO implements Serializable {

    /**
     * 日志类型
     */
    private Integer logType;
    /**
     * 调用类名
     */
    private String reqClass;
    /**
     * 调用方法名
     */
    private String methodName;
    /**
     * 接口描述
     */
    private String apiMsg;
    /**
     * 请求路径
     */
    private String requestUrl;
    /**
     * 调用的参数
     */
    private String request;
    /**
     * 方法返回结果
     */
    private String response;
    /**
     * 起始时间
     */
    private String beginTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 执行时长，毫秒
     */
    private Long costTime;
    /**
     * 备注
     */
    private String remark;
}
