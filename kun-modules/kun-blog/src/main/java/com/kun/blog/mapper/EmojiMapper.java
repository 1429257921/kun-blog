package com.kun.blog.mapper;

import com.kun.blog.entity.pojo.Emoji;
import com.kun.common.database.mapper.CoreMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 坤坤表情包(Emoji)表数据库访问层
 *
 * @author gzc
 * @since 2022-09-30 22:28:03
 */
@Mapper
public interface EmojiMapper extends CoreMapper<Emoji> {

}