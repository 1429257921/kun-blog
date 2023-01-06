package com.kun.blog.service.impl;

import cn.hutool.core.lang.UUID;
import com.kun.blog.entity.po.ChatMessages;
import com.kun.blog.entity.po.ChatMessagesType;
import com.kun.blog.mapper.ChatMessagesMapper;
import com.kun.blog.mapper.ChatMessagesTypeMapper;
import com.kun.blog.service.IChatMessagesService;
import com.kun.common.database.service.impl.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gzc
 * @since 2022-10-29 10:01:42
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true, rollbackFor = Exception.class)
public class ChatMessagesServiceImpl extends BaseServiceImpl<ChatMessagesMapper, ChatMessages> implements IChatMessagesService {

    private final ChatMessagesTypeMapper chatMessagesTypeMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public Object test(Integer id) {
        log.info("执行test2");
        for (int i = 0; i < 10; i++) {
            ChatMessagesType chatMessagesType = new ChatMessagesType();
            chatMessagesType.setCmtName("测试" + i);
            chatMessagesTypeMapper.insert(chatMessagesType);
        }
//        throw new BizException("失败了回滚吧111");
        return null;
    }

    public static void main(String[] args) {
        System.out.println(UUID.fastUUID().toString(true));
    }
}
