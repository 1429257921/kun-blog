package com.kun.common.redis.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防止重复提交
 *
 * @author: gzc
 * @date: 2022-1-20 9:39
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRepeatSubmit {

	/**
	 * 设置请求锁定时间(单位:秒)
	 */
	int lockTime() default 20;

}
