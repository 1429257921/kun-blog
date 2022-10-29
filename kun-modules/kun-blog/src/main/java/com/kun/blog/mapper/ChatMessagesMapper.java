package com.kun.blog.mapper;

import com.kun.blog.entity.po.ChatMessages;
import com.kun.common.database.mapper.CoreMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 聊天记录表chat_messages表持久层接口
 *
 * @author gzc
 * @since 2022-10-29 10:01:42
 */
public interface ChatMessagesMapper extends CoreMapper<ChatMessages> {

}
