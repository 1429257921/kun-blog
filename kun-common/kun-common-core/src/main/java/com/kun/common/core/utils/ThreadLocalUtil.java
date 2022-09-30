package com.kun.common.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 本地线程变量工具类
 *
 * @author: gzc
 * @createTime: 2022-1-20 14:52
 * @since: 1.0
 **/
public class ThreadLocalUtil {

	/**
	 * 可实现父子线程之间的值传递
	 */
	private static final ThreadLocal<Map<String, Object>> threadLocal;

	/**
	 * 可实现主线程与线程池中的线程之间的值传递（需要引入依赖）
	 */
//	ThreadLocal<String> threadLocal = new TransmittableThreadLocal<>();

	/**
	 * 初始化
	 */
	static {
//		threadLocal = new InheritableThreadLocal<>();
//		threadLocal.set(new HashMap<>(10));
		threadLocal = ThreadLocal.withInitial(() -> new HashMap<>(10));
	}

	/**
	 * 获取
	 *
	 * @param
	 * @return: java.util.Map<java.lang.String, java.lang.Object>
	 * @author: gzc
	 * @date: 2022-1-20 14:55
	 */
	public static Map<String, Object> getThreadLocal() {
		return threadLocal.get();
	}

	/**
	 * 获取
	 *
	 * @param key
	 * @return: T
	 * @author: gzc
	 * @date: 2022-1-20 14:55
	 */
	public static <T> T get(String key) {
		return get(key, null);
	}

	/**
	 * 获取
	 *
	 * @param key
	 * @param defaultValue
	 * @return: T
	 * @author: gzc
	 * @date: 2022-1-20 14:55
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(String key, T defaultValue) {
		Map<String, Object> map = threadLocal.get();
		if (map == null) {
			map = new HashMap<>(1);
		}
		return (T) Optional.ofNullable(map.get(key)).orElse(defaultValue);
	}

	/**
	 * 赋值
	 *
	 * @param key
	 * @param value
	 * @return: void
	 * @author: gzc
	 * @date: 2022-1-20 14:55
	 */
	public static void set(String key, Object value) {
		Map<String, Object> map = Optional.ofNullable(threadLocal.get()).orElse(new HashMap<>(10));
		map.put(key, value);
	}

	/**
	 * 赋值
	 *
	 * @param keyValueMap
	 * @return: void
	 * @author: gzc
	 * @date: 2022-1-20 14:54
	 */
	public static void set(Map<String, Object> keyValueMap) {
		Map<String, Object> map = Optional.ofNullable(threadLocal.get()).orElse(new HashMap<>(10));
		map.putAll(keyValueMap);

	}

	/**
	 * 移除
	 *
	 * @param
	 * @return: void
	 * @author: gzc
	 * @date: 2022-1-20 14:54
	 */
	public static void remove() {
		threadLocal.remove();
	}

	/**
	 * 移除
	 *
	 * @param key
	 * @return: T
	 * @author: gzc
	 * @date: 2022-1-20 14:54
	 */
	@SuppressWarnings("unchecked")
	public static <T> T remove(String key) {
		Map<String, Object> map = threadLocal.get();
		return (T) map.remove(key);
	}

}
