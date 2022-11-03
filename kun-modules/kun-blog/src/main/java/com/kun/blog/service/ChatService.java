package com.kun.blog.service;

import com.kun.blog.entity.vo.ChatListVO;

import java.util.List;

/**
 * TODO
 *
 * @author gzc
 * @since 2022/11/3 15:16
 **/
public interface ChatService {
    /**
     * 获取聊天列表信息
     *
     * @return
     */
    List<ChatListVO> getChatList();
}
