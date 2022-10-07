package com.kun.blog.mapper;

import com.kun.blog.entity.po.KunLog;
import com.kun.common.database.mapper.CoreMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统日志kun_log表持久层接口
 *
 * @author gzc
 * @since 2022-10-07 21:05:29
 */
public interface KunLogMapper extends CoreMapper<KunLog> {

}
