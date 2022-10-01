package com.kun.blog.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kun.blog.entity.po.Emoji;
import com.kun.blog.entity.po.EmojiType;
import com.kun.blog.mapper.EmojiMapper;
import com.kun.blog.mapper.EmojiTypeMapper;
import com.kun.common.redis.vo.KunResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 *
 * @author gzc
 * @since 2022/9/30 22:08
 **/
@RequiredArgsConstructor
@RequestMapping("/test/")
@RestController
public class TestController {
    private final EmojiMapper emojiMapper;
    private final EmojiTypeMapper emojiTypeMapper;

    @RequestMapping("get")
    public KunResult<?> get(Integer id) {
        System.out.println("id->" + id);
        Emoji emoji = new LambdaQueryChainWrapper<>(emojiMapper)
                .eq(Emoji::getId, id)
                .one();
        System.out.println(JSON.toJSONString(emoji));

        EmojiType emojiType = new LambdaQueryChainWrapper<>(emojiTypeMapper)
                .eq(EmojiType::getId, emoji.getTypeId())
                .one();
        System.out.println(JSON.toJSONString(emojiType));
        return KunResult.ok(emoji);
    }
}
