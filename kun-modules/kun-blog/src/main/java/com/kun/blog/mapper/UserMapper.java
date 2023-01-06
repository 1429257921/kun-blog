package com.kun.blog.mapper;

import com.kun.blog.entity.po.User;
import com.kun.common.database.mapper.CoreMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 坤坤云用户表k_user表持久层接口
 *
 * @author gzc
 * @since 2023-01-05 17:59:28
 */
public interface UserMapper extends CoreMapper<User> {

}
