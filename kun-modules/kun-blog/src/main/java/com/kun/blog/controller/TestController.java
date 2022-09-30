package com.kun.blog.controller;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kun.common.redis.vo.KunResult;
import com.kun.blog.entity.pojo.Emoji;
import com.kun.blog.mapper.EmojiMapper;
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

    @RequestMapping("get")
    public KunResult<?> get(Integer id) {
        System.out.println("id->" + id);
        Emoji emoji = new LambdaQueryChainWrapper<Emoji>(emojiMapper)
                .eq(Emoji::getId, id)
                .one();
        return KunResult.ok(emoji);
    }
}
