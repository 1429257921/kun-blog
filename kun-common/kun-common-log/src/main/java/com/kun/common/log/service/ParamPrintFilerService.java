package com.kun.common.log.service;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 参数日志打印过滤
 *
 * @author gzc
 * @since 2022/9/30 20:39
 */
public interface ParamPrintFilerService {


    /**
     * 切入点请求参数过滤
     *
     * @param joinPoint
     * @param reqJson
     */
    String reqJoinPointFiler(ProceedingJoinPoint joinPoint, String reqJson);

    /**
     * 切入点响应数据过滤
     *
     * @param joinPoint
     * @param respObj
     */
    String respJoinPointFiler(ProceedingJoinPoint joinPoint, Object respObj);

}
