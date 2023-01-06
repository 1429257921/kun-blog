package com.kun.blog.service.impl;

import cn.hutool.core.lang.UUID;
import com.kun.blog.entity.po.ChatMessages;
import com.kun.blog.entity.po.ChatMessagesType;
import com.kun.blog.entity.vo.ChatListVO;
import com.kun.blog.mapper.ChatMessagesMapper;
import com.kun.blog.mapper.ChatMessagesTypeMapper;
import com.kun.blog.security.dto.JwtUser;
import com.kun.blog.service.ChatService;
import com.kun.blog.service.IChatMessagesService;
import com.kun.blog.service.IChatMessagesTypeService;
import com.kun.blog.util.SecurityUtils;
import com.kun.common.core.exception.BizException;
import com.kun.common.core.utils.io.KunIoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
    private final ChatMessagesTypeMapper chatMessagesTypeMapper;
    private final IChatMessagesService iChatMessagesService;
    private final IChatMessagesTypeService iChatMessagesTypeService;

    @Override
    public List<ChatListVO> getChatList() {
        JwtUser jwtUser = (JwtUser) SecurityUtils.getUserDetails();
        // 查询好友信息

        // 查询最新十条聊天记录


        return null;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public Object test(Integer id) {
        log.info("开始测试");

//        for (int i = 0; i < 10; i++) {
//            if (i == 5) {
//                throw new BizException("出现异常，开始回滚");
//            }
//            ChatMessagesType chatMessagesType = new ChatMessagesType();
//            chatMessagesType.setCmtName("石头人啊");
//            chatMessagesTypeMapper.insert(chatMessagesType);
//        }
//        return null;
        if (id == 1) {
            save1();
            save2();
        } else {
            ChatMessages chatMessages = new ChatMessages();
            chatMessages.setMarkRead(1);
            chatMessages.setCmContent("我草来");
            chatMessages.setStatus(0);
            chatMessages.setFromUid(1);
            chatMessages.setToUid(1);
            chatMessages.setTypeId(1);
            chatMessagesMapper.insert(chatMessages);
            ChatMessagesType chatMessagesType = new ChatMessagesType();
            chatMessagesType.setCmtName("石头人啊");
            chatMessagesTypeMapper.insert(chatMessagesType);
            iChatMessagesService.test(id);
            throw new BizException("开了多久埃里克森简单");
        }
        return null;
    }

    public void save1() {
        ChatMessages chatMessages = new ChatMessages();
        chatMessages.setMarkRead(1);
        chatMessages.setCmContent("我草来");
        chatMessages.setStatus(0);
        chatMessages.setFromUid(1);
        chatMessages.setToUid(1);
        chatMessages.setTypeId(1);
        chatMessagesMapper.insert(chatMessages);
    }

    public void save2() {
        ChatMessagesType chatMessagesType = new ChatMessagesType();
        chatMessagesType.setCmtName("石头人啊");
        chatMessagesTypeMapper.insert(chatMessagesType);
    }

    public static void main(String[] args) {
        System.out.println(UUID.fastUUID().toString(false).length());
    }


}
