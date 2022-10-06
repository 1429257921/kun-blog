package com.kun.blog.mapper;

import com.kun.blog.entity.po.Emoji;
import com.kun.common.database.mapper.CoreMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 坤坤表情包emoji表持久层接口
 *
 * @author gzc
 * @since 2022-10-01 18:49:43
 */
//@Mapper
public interface EmojiMapper extends CoreMapper<Emoji> {

}
