package com.kun.common.redis.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.kun.common.core.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.util.Collections;

/**
 * redis分布式锁工具类
 * 思路：
 * 用SETNX命令，SETNX只有在key不存在时才返回成功。这意味着只有一个线程可以成功运行SETNX命令，
 * 而其他线程会失败，然后不断重试，直到它们能建立锁。
 * 然后使用脚本来创建锁，因为一个redis脚本同一时刻只能运行一次。
 *
 * @author: gzc
 * @date: 2022-1-4 13:42
 */
@Slf4j
@SuppressWarnings("all")
public class RedisLockUtil {

    /**
     * 成功标识
     */
    private static final Long SUCCESS = 1L;

    /**
     * 加锁lua脚本
     */
    private static final String SCRIPT_LOCK = "if redis.call('setNx', KEYS[1], ARGV[1]) == 1 then redis.call('pexpire', KEYS[1],ARGV[2]) return 1 else return 0 end";
    /**
     * 解锁lua脚本
     */
    private static final String SCRIPT_UNLOCK = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    /**
     * 接口限流lua脚本
     */
    private static final String API_LIMIT = "local c = redis.call('get',KEYS[1]) or '0' if tonumber(c) > tonumber(ARGV[1]) then return tonumber(c) end c = redis.call('incr',KEYS[1]) if tonumber(c) == 1 then redis.call('expire',KEYS[1],ARGV[2]) end return tonumber(c)";
    /**
     * 加锁脚本sha1值
     */
    private static final String SCRIPT_LOCK_SHA1 = encrypt(SCRIPT_LOCK);
    /**
     * 解锁脚本sha1值
     */
    private static final String SCRIPT_UNLOCK_SHA1 = encrypt(SCRIPT_UNLOCK);
    /**
     * 接口限流脚本sha1值
     */
    private static final String SCRIPT_LIMIT_SHA1 = encrypt(API_LIMIT);


    private static RedisTemplate staticRedisTemplate;

    /**
     * redis重试次数
     */
    private static int RETRY_NUM = 5;
    /**
     * 锁过期时长 2小时
     */
    private static int lockTime = 2 * 60 * 60 * 1000;

    /**
     * 依赖注入
     * 参考博客:https://blog.csdn.net/qq_40084325/article/details/111387068
     */
    @Resource
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        log.info("初始化staticRedisTemplate属性");
        staticRedisTemplate = redisTemplate;
    }

    /**
     * 尝试获取分布式锁 (使用lua脚本可以保证加锁的原子性)
     * 对于 Redis 集群则无法使用
     * 支持重复，线程安全
     *
     * @param lockKey   锁key
     * @param requestId 请求标识 (采用UUID)
     * @param seconds   锁过期时间(单位秒)，多少秒后这把锁自动释放(最大值为21,4748,3647)
     * @return 返回true表示拿到锁
     */
    public synchronized static boolean tryLock(final String lockKey,
                                               final String requestId,
                                               final int seconds) {
        Object result = null;
        for (int i = 1; i <= RETRY_NUM; i++) {
            String ex = null;
            try {
                // 执行lua脚本
                result = staticRedisTemplate.execute(
                        new RedisScript<Long>() {
                            @Override
                            public String getSha1() {
                                return SCRIPT_LOCK_SHA1;
                            }

                            @Override
                            public Class<Long> getResultType() {
                                return Long.class;
                            }

                            @Override
                            public String getScriptAsString() {
                                return SCRIPT_LOCK;
                            }

                        }, Collections.singletonList(lockKey),// KEYS[1]
                        requestId, // ARGV[1]
                        seconds // ARGV[2]
                );
            } catch (Exception e) {
                ex = ExceptionUtil.stacktraceToString(e);
                log.error("尝试获取分布式锁发生异常：{}", e);
            }
            // 调用成功则退出重试
            if (ObjectUtil.isNotEmpty(result)) {
                break;
            }
            // 最后一次释放锁失败则抛出异常
            if (i == RETRY_NUM) {
                StrBuilder sb = new StrBuilder();
                sb.append("锁名为[");
                sb.append(lockKey);
                sb.append("], 超过获取锁最大重试次数, 异常原因为\n");
                sb.append(ex);
                throw new BizException("获取锁发生异常", sb.toString());
            }
        }
        // 返回结果
        return SUCCESS.equals(result);
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁key
     * @param requestId 请求标识 (采用UUID)
     * @return 返回true表示释放锁成功
     */
    public static boolean releaseLock(String lockKey, String requestId) {
        // 执行lua脚本
        Object result = null;
        for (int i = 1; i <= RETRY_NUM; i++) {
            String ex = null;
            try {
                result = staticRedisTemplate.execute(
                        new RedisScript<Long>() {
                            @Override
                            public String getSha1() {
                                return SCRIPT_UNLOCK_SHA1;
                            }

                            @Override
                            public Class<Long> getResultType() {
                                return Long.class;
                            }

                            @Override
                            public String getScriptAsString() {
                                return SCRIPT_UNLOCK;
                            }
                        }, Collections.singletonList(lockKey),
                        requestId);
            } catch (Exception e) {
                ex = ExceptionUtil.stacktraceToString(e);
                log.error("第[{}]次,释放分布式锁[{}]发生异常:{}", i, lockKey, e);
            }
            // 调用成功则退出重试
            if (ObjectUtil.isNotEmpty(result)) {
                break;
            }
            // 最后一次释放锁失败则抛出异常
            if (i == RETRY_NUM) {
                StrBuilder sb = new StrBuilder();
                sb.append("锁名为[");
                sb.append(lockKey);
                sb.append("], 超过释放锁最大重试次数, 异常原因为\n");
                sb.append(ex);
                throw new BizException("释放锁发生异常", sb.toString());
            }
        }
        // 返回结果
        return SUCCESS.equals(result);
    }

    /**
     * 接口限流
     *
     * @param lockKey 锁key
     * @param count   限制访问次数
     * @param period  多少时间内，单位秒
     * @return: java.lang.Number
     * @author: gzc
     * @date: 2022-1-20 11:23
     */
    public static Long limit(String lockKey, int count, int period) {
        // 执行lua脚本
        Object result = staticRedisTemplate.execute(
                new RedisScript<Long>() {
                    @Override
                    public String getSha1() {
                        return SCRIPT_LIMIT_SHA1;
                    }

                    @Override
                    public Class<Long> getResultType() {
                        return Long.class;
                    }

                    @Override
                    public String getScriptAsString() {
                        return API_LIMIT;
                    }

                }, Collections.singletonList(lockKey), count, period);
        return Long.valueOf(StrUtil.utf8Str(result));
    }


    private static final String ZERO = "0";
    private static final String ALGORITHM = "SHA1";

    /**
     * sha1加密
     *
     * @param str
     * @return 返回十六进制的字符串形式，全部小写
     */
    private static String encrypt(String str) {
        if (StrUtil.isBlank(str)) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
            messageDigest.update(str.getBytes());
            return byte2hex(messageDigest.digest());
        } catch (Exception e) {
            throw new BizException("redis脚本sha1加密异常:" + e.getMessage(), e);
        }
    }

    /**
     * 二进制转十六进制字符串
     */
    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append(ZERO);
            }
            sign.append(hex);
        }
        return sign.toString();
    }

}
