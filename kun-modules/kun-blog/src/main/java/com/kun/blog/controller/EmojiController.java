package com.kun.blog.controller;

import com.alibaba.fastjson.JSON;
import com.kun.blog.entity.dto.EmojiSearchReq;
import com.kun.blog.service.IEmojiService;
import com.kun.common.log.anno.APIMessage;
import com.kun.common.redis.vo.KunResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author gzc
 * @since 2022/10/3 21:46
 **/
@RequestMapping("/api/emoji/")
@RequiredArgsConstructor
@RestController
public class EmojiController {

    private final IEmojiService iEmojiService;

    @APIMessage("搜索坤坤表情包")
    @PostMapping("search")
    public KunResult<?> search(EmojiSearchReq emojiSearchReq) {
        System.out.println("emojiSearchReq->" + JSON.toJSONString(emojiSearchReq));
        if (!"1".equals(emojiSearchReq.getSearchContent())) {
            return KunResult.ok();
        }
        return KunResult.ok(iEmojiService.list());
    }
}
