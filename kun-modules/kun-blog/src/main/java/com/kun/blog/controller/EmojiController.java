package com.kun.blog.controller;

import com.kun.blog.entity.req.EmojiSearchReq;
import com.kun.blog.service.IEmojiService;
import com.kun.common.log.anno.APIMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 表情包控制层
 *
 * @author gzc
 * @since 2022/10/3 21:46
 **/
@RequestMapping("/api/emoji/")
@RequiredArgsConstructor
@RestController
public class EmojiController {

    private final IEmojiService iEmojiService;

    /**
     * 搜索坤坤表情包
     *
     * @author gzc
     * @since 2022/10/6 13:13
     */
    @APIMessage("搜索坤坤表情包")
    @GetMapping("search")
    public ResponseEntity<Object> search(@Validated EmojiSearchReq emojiSearchReq, Pageable pageable) {
        return new ResponseEntity<>(iEmojiService.search(emojiSearchReq, pageable), HttpStatus.OK);
    }


}
