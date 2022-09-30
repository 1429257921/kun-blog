package com.kun.common.log.service;


import com.kun.common.log.entity.LogDTO;

/**
 * 日志业务(需要将接口请求响应信息入库则需要自行实现此接口)
 *
 * @author: gzc
 * @createTime: 2022-1-10 14:52
 * @since: 1.0
 **/
public interface ComLogService {

	/**
	 * 日志入库(入库失败会抛出异常)
	 *
	 * @param logDTO 通用日志DTO对象
	 * @return: void
	 * @author: gzc
	 * @date: 2022-1-10 14:57
	 */
	void insertDB(LogDTO logDTO);
}
