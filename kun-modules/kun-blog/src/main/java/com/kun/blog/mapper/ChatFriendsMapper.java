package com.kun.blog.mapper;

import com.kun.blog.entity.po.ChatFriends;
import com.kun.common.database.mapper.CoreMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 聊天好友表chat_friends表持久层接口
 *
 * @author gzc
 * @since 2022-10-29 10:01:42
 */
public interface ChatFriendsMapper extends CoreMapper<ChatFriends> {

}
