package com.kun.blog.controller;

import com.kun.blog.service.ChatService;
import com.kun.common.log.anno.APIMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author gzc
 * @since 2022/11/3 15:13
 **/
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/chat/")
public class ChatController {

    private final ChatService chatService;

    /**
     * 获取聊天列表信息
     *
     * @return
     */
    @APIMessage("获取聊天列表")
    @GetMapping("getChatList")
    public ResponseEntity<Object> getChatList() {
        return new ResponseEntity<>(chatService.getChatList(), HttpStatus.OK);
    }

}
