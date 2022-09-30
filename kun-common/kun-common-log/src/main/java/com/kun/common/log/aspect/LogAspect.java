package com.kun.common.log.aspect;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kun.common.core.constants.ComRegexConstant;
import com.kun.common.core.constants.ThreadLocalMapConstants;
import com.kun.common.core.exception.Assert;
import com.kun.common.core.utils.ThreadLocalUtil;
import com.kun.common.log.anno.APIMessage;
import com.kun.common.log.entity.LogDTO;
import com.kun.common.log.enums.LogTypeEnum;
import com.kun.common.log.service.ComLogService;
import com.kun.common.log.service.ParamPrintFilerService;
import com.kun.common.redis.util.WebContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * 请求响应日志切面
 *
 * @author gzc
 * @since 2022/9/30 20:38
 */
@Slf4j
@Aspect
public class LogAspect {

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired(required = false)
    private ComLogService comLogService;
    @Autowired(required = false)
    private ParamPrintFilerService paramPrintFilerService;

    /**
     * 环绕通知(切入点为注解)
     *
     * @param joinPoint
     * @return java.lang.Object
     * @author gzc
     * @since 2022/9/30 20:38
     */
    @Around("@annotation(com.kun.common.log.anno.APIMessage)")
    public Object aroundApiLogInsertDB(ProceedingJoinPoint joinPoint) throws Throwable {
        Object response = null;
        Exception exception = null;
        // 起始时间
        DateTime beginTime = DateUtil.date();
        // 结束时间
        DateTime endTime = DateUtil.date();
        String apiMsg = "";
        try {
            // 请求参数输出日志
            reqLogPrint(joinPoint);
            apiMsg = ThreadLocalUtil.get(ThreadLocalMapConstants.API_MESSAGE);
            log.info("开始执行{} 业务逻辑", apiMsg);
            try {
                response = joinPoint.proceed();
            } catch (Exception exp) {
                exception = exp;
            }
            // 结束时间
            endTime = DateUtil.date();

            if (exception != null) {
                throw exception;
            }
            // 获取返回值
//			String result = ObjectUtil.isNotEmpty(response) ? String.valueOf(response) : "";
            Object result = response;

            // 正常信息日志入库
            LogDTO logDTO = getLogInfo(joinPoint, result, null,
                    LogTypeEnum.SYSTEM_LOG.getCode(), beginTime, endTime);

            insertDB(apiMsg, logDTO, joinPoint);
        } catch (Throwable th) {
            // 结束时间
            endTime = DateUtil.date();
            if (th instanceof Exception) {
                exception = (Exception) th;
            }
            // 错误信息日志入库
            LogDTO logDTO = getLogInfo(joinPoint, null, exception,
                    LogTypeEnum.ERROR_LOG.getCode(), beginTime, endTime);
            insertDB(apiMsg, logDTO, joinPoint);
            throw th;
        } finally {
            ThreadLocalUtil.remove();
            log.info("结束执行{} 业务逻辑, 耗时->{}", apiMsg, DateUtil.betweenMs(beginTime, endTime));
        }
        return response;
    }

    /**
     * 日志入库
     *
     * @param apiMsg    接口描述
     * @param logDTO    日志对象
     * @param joinPoint 切入点
     */
    private void insertDB(String apiMsg, LogDTO logDTO, ProceedingJoinPoint joinPoint) {
        APIMessage apiMessage = getAPIMessage(joinPoint);
        try {
            // 判断是否入库
            if (apiMessage.reqLogInsertDB()) {
                if (comLogService != null) {
                    comLogService.insertDB(logDTO);
                }
            }
        } catch (Exception e) {
            log.error("{}切面日志入库发生异常, 异常原因为{}", apiMsg, e);
//			throw new BizException(apiMsg + "切面日志入库发生异常", e);
        }
    }

    /**
     * 打印请求参数日志
     *
     * @param joinPoint 连接点对象
     * @return: void
     * @author: gzc
     */
    private void reqLogPrint(ProceedingJoinPoint joinPoint) {
        APIMessage apiMessage = getAPIMessage(joinPoint);
        // 接口描述
        String apiMsg = "[" + apiMessage.value() + "]接口";
        // 赋值接口描述
        ThreadLocalUtil.set(ThreadLocalMapConstants.API_MESSAGE, apiMsg);
        log.info("请求地址->{}", WebContextUtil.getServletPath());

        // 是否打印请求参数
        if (apiMessage.printReqParam()) {
            log.info("请求token->{}", WebContextUtil.getToken());
            String reqParam = reqParamFilerFile(joinPoint);
            log.info("{}请求参数->\n{}", apiMsg, reqParam);
            Assert.notBlank(reqParam, "请求参数为空");
        }
    }

    /**
     * 请求参数过滤
     *
     * @param joinPoint
     */
    private String reqParamFilerFile(ProceedingJoinPoint joinPoint) {
        // 过滤body请求参数
        String bodyParam = paramPrintFilerService == null
                ? WebContextUtil.getBodyParam()
                : paramPrintFilerService.reqJoinPointFiler(joinPoint, WebContextUtil.getBodyParam());
        // 去除特殊字符
        bodyParam = Pattern.compile(ComRegexConstant.SPECIAL_CHARACTERS).matcher(bodyParam).replaceAll("").trim();

        // 过滤url请求参数
        String urlParam = paramPrintFilerService == null
                ? WebContextUtil.getURLParam()
                : paramPrintFilerService.reqJoinPointFiler(joinPoint, WebContextUtil.getURLParam());
        // 去除特殊字符
        urlParam = Pattern.compile(ComRegexConstant.SPECIAL_CHARACTERS).matcher(urlParam).replaceAll("").trim();
        if (StrUtil.isAllBlank(bodyParam, urlParam)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("body请求参数->");
        sb.append(bodyParam);
        sb.append("\n");
        sb.append("url请求参数->");
        sb.append(urlParam);
        return sb.toString();
    }

    /**
     * 获取注解
     *
     * @param joinPoint 切入点
     * @return 注解信息
     */
    private APIMessage getAPIMessage(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        APIMessage apiMessage = method.getAnnotation(APIMessage.class);
        // 获取接口上的注解
        return apiMessage;
    }

    /**
     * 构建Log对象
     *
     * @param joinPoint 连接点对象
     * @param result    正常返回结果
     * @param exception 错误返回结果
     * @param logType   日志类型
     * @param beginTime 起始时间
     * @param endTime   结束时间
     * @return: com.jiahe.ylq.common.entity.dto.LogDTO
     * @author: gzc
     */
    private LogDTO getLogInfo(ProceedingJoinPoint joinPoint, Object result,
                              Exception exception, Integer logType,
                              DateTime beginTime, DateTime endTime) {
        LogDTO logDTO = new LogDTO();
        try {
            // 日志类型
            logDTO.setLogType(logType);
            // 起始时间
            logDTO.setBeginTime(beginTime.toString());
            // 结束时间
            logDTO.setEndTime(endTime.toString());
            // 接口调用耗时
            logDTO.setCostTime(DateUtil.betweenMs(beginTime, endTime));
            // 接口描述
            logDTO.setApiMsg(WebContextUtil.getApiMsg());
            // 获取请求路径
            logDTO.setRequestUrl(WebContextUtil.getServletPath());

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取异常信息
            if (exception != null) {
                logDTO.setResponse(ExceptionUtil.stacktraceToOneLineString(exception));
            }
            // 获取接口返回结果
            else if (ObjectUtil.isNotEmpty(result)) {
                String resultJson = serial(result);
                logDTO.setResponse(resultJson);
                if (paramPrintFilerService != null) {
                    String filerStr = paramPrintFilerService.respJoinPointFiler(joinPoint, result);
                    if (StrUtil.isNotBlank(filerStr)) {
                        logDTO.setResponse(filerStr);
                    }
                }
            }
            // 获取类的包路径
            String signName = signature.getDeclaringTypeName();
            logDTO.setReqClass(signName);
            // 获取方法名
            logDTO.setMethodName(signature.getName());
            // 获取请求参数
            Object[] args = joinPoint.getArgs();
            if (ArrayUtil.isNotEmpty(args)) {
                logDTO.setRequest(serial(args));
                if (paramPrintFilerService != null) {
                    logDTO.setRequest(paramPrintFilerService.reqJoinPointFiler(joinPoint, reqParamFilerFile(joinPoint)));
                }
            }
        } catch (Exception e) {
            log.error("切面封装日志信息异常:{}", e);
//			logDTO.setResponse(ExceptionUtils.getExceptionDetails(exception, false));
        }
        return logDTO;
    }

    private static String serial(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception ex) {
            return obj.toString();
        }
    }

}
