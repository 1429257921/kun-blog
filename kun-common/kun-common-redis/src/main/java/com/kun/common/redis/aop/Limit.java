package com.kun.common.redis.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口限流注解
 *
 * @author: gzc
 * @createTime: 2022-1-20 11:00
 * @since: 1.0
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

	/**
	 * 资源名称，用于描述接口功能
	 */
	String name() default "";

	/**
	 * 资源 key
	 */
	String key() default "";

	/**
	 * key 前缀
	 */
	String prefix() default "";

	/**
	 * 多少时间内，单位秒
	 */
	int period() default 10;

	/**
	 * 限制访问次数
	 */
	int count() default 5;

	/**
	 * 限制类型(默认用IP)
	 */
	LimitType limitType() default LimitType.IP;

}
