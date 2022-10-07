package com.kun.common.redis.aop;

import cn.hutool.core.lang.UUID;
import com.kun.common.core.exception.BizException;
import com.kun.common.redis.util.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 防止重复提交aop切面
 *
 * @author gzc
 * @since 2022/10/7 18:13
 */
@Slf4j
@Aspect
public class RepeatSubmitAspect {

    @Around("@annotation(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint pjp, NoRepeatSubmit noRepeatSubmit) throws Throwable {
        int lockSeconds = noRepeatSubmit.lockTime();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 获取token
        String bearerToken = request.getHeader("Authorization");
        String[] tokens = bearerToken.split(" ");
        String token = tokens[1];
        String path = request.getServletPath();
        // 拼接key
        String lockKey = token + path;
        // 请求唯一标识(UUID)
        String requestId = UUID.fastUUID().toString();

        boolean isSuccess = RedisLockUtil.tryLock(lockKey, requestId, lockSeconds);
//		log.info("tryLock lockKey = [{}], requestId = [{}]", lockKey, requestId);

        if (isSuccess) {
//			log.info("tryLock success, lockKey = [{}], requestId = [{}]", lockKey, requestId);
            // 获取锁成功
            Object result;
            try {
                // 执行进程
                result = pjp.proceed();
            } finally {
                // 解锁
                RedisLockUtil.releaseLock(lockKey, requestId);
//				log.info("releaseLock success, lockKey = [{}], requestId = [{}]", lockKey, requestId);
            }
            return result;

        } else {
            // 获取锁失败，认为是重复提交的请求
//			log.info("tryLock fail, lockKey = [{}]", lockKey);
            throw new BizException("重复请求，请稍后再试");
        }
    }
}
