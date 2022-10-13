package com.kun.blog.controller;

import com.kun.blog.entity.req.EmojiGetConfigReq;
import com.kun.blog.entity.req.EmojiSearchReq;
import com.kun.blog.service.IEmojiService;
import com.kun.common.log.anno.APIMessage;
import com.kun.common.redis.aop.Limit;
import com.kun.common.redis.constants.CacheConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
     * @param emojiSearchReq
     * @author gzc
     * @since 2022/10/6 13:13
     */
    @APIMessage("搜索坤坤表情包")
    @GetMapping("search")
    public ResponseEntity<Object> search(@Validated EmojiSearchReq emojiSearchReq, Pageable pageable) {
        return new ResponseEntity<>(iEmojiService.search(emojiSearchReq, pageable), HttpStatus.OK);
    }

    /**
     * 获取配置信息
     *
     * @param emojiSearchReq
     * @author gzc
     * @since 2022/10/7 13:54
     */
    @Limit(prefix = CacheConstants.PC_LIMIT_PREFIX)
    @APIMessage("获取配置信息")
    @GetMapping("getConfig")
    public ResponseEntity<Object> getConfig(@Validated EmojiGetConfigReq emojiSearchReq) {
        return new ResponseEntity<>(iEmojiService.getConfig(emojiSearchReq), HttpStatus.OK);
    }

    /**
     * 增加坤坤表情包
     *
     * @param file
     * @author gzc
     * @since 2022/10/6 13:12
     */
    @Limit(prefix = CacheConstants.PC_LIMIT_PREFIX)
    @APIMessage(value = "增加坤坤表情包", printReqParam = false)
    @GetMapping("upload")
    public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(iEmojiService.upload(file), HttpStatus.OK);
    }

}
