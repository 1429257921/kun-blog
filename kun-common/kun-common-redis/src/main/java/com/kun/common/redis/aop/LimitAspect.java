package com.kun.common.redis.aop;

import cn.hutool.core.util.StrUtil;
import com.kun.common.core.exception.BizException;
import com.kun.common.core.utils.ip.IPUtil;
import com.kun.common.redis.util.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 限制接口访问频率切面
 *
 * @author gzc
 * @since 2022/10/7 18:13
 */
@Slf4j
@Aspect
public class LimitAspect {

    @Around("@annotation(limit)")
    public Object around(ProceedingJoinPoint joinPoint, Limit limit) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        LimitType limitType = limit.limitType();
        String key = limit.key();
        if (StrUtil.isBlank(key)) {
            if (limitType == LimitType.IP) {
                key = IPUtil.getIp(request);
            } else {
                key = signatureMethod.getName();
            }
        }
        // 拼接key
        String lockKey = StrUtil.join(limit.prefix(), "_", key, "_", request.getRequestURI().replaceAll("/", "_"));
        // redis操作
        Number count = RedisLockUtil.limit(lockKey, limit.count(), limit.period());
        if (null != count && count.intValue() <= limit.count()) {
//			log.info("第{}次访问key为 {}，描述为 [{}] 的接口", count.intValue(), lockKey, limit.name());
            return joinPoint.proceed();
        } else {
            throw new BizException("操作频繁, 请稍后重试");
        }
    }


}
