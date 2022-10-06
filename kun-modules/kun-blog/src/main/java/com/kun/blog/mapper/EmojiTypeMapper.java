package com.kun.blog.mapper;

import com.kun.blog.entity.po.EmojiType;
import com.kun.common.database.mapper.CoreMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 表情包类型emoji_type表持久层接口
 *
 * @author gzc
 * @since 2022-10-01 18:49:43
 */
//@Mapper
public interface EmojiTypeMapper extends CoreMapper<EmojiType> {

}
