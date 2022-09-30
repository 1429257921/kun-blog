package com.kun.common.redis.service;

import com.kun.common.redis.util.Locker;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author gzc
 * @since 2022/9/21 5:29
 **/
@RequiredArgsConstructor
public class RedissonService {
    public final Locker locker;

    /**
     * 获取锁
     *
     * @param lockKey
     */
    public void lock(String lockKey) {
        locker.lock(lockKey);
    }

    /**
     * 释放锁
     *
     * @param lockKey
     */
    public void unlock(String lockKey) {
        locker.unlock(lockKey);
    }

    /**
     * 获取锁，超时释放
     *
     * @param lockKey
     * @param timeout
     */
    public void lock(String lockKey, int timeout) {
        locker.lock(lockKey, timeout);
    }

    /**
     * 获取锁，超时释放，指定时间单位
     *
     * @param lockKey
     * @param unit
     * @param timeout
     */
    public void lock(String lockKey, TimeUnit unit, int timeout) {
        locker.lock(lockKey, unit, timeout);
    }

    /**
     * 尝试获取锁，获取到立即返回true,获取失败立即返回false
     *
     * @param lockKey
     * @return
     */
    public boolean tryLock(String lockKey) {
        return locker.tryLock(lockKey);
    }

    /**
     * 尝试获取锁，在给定的waitTime时间内尝试，获取到返回true,获取失败返回false,获取到后再给定的leaseTime时间超时释放
     *
     * @param lockKey
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @return
     * @throws InterruptedException
     */
    public boolean tryLock(String lockKey, long waitTime, long leaseTime,
                           TimeUnit unit) throws InterruptedException {
        return locker.tryLock(lockKey, waitTime, leaseTime, unit);
    }

    /**
     * 锁是否被任意一个线程持有
     *
     * @param lockKey
     * @return
     */
    public boolean isLocked(String lockKey) {
        return locker.isLocked(lockKey);
    }
}


