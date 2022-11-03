package com.kun.blog.service.impl;

import com.kun.blog.entity.vo.ChatListVO;
import com.kun.blog.mapper.ChatMessagesMapper;
import com.kun.blog.security.dto.JwtUser;
import com.kun.blog.service.ChatService;
import com.kun.blog.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 *
 * @author gzc
 * @since 2022/11/3 15:16
 **/
@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private final ChatMessagesMapper chatMessagesMapper;


    @Override
    public List<ChatListVO> getChatList() {
        JwtUser jwtUser = (JwtUser) SecurityUtils.getUserDetails();
        Long id = jwtUser.getId();
        // 查询好友信息

        // 查询最新十条聊天记录


        return null;
    }
}
