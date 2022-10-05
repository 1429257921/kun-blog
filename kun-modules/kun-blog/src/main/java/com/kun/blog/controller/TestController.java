package com.kun.blog.controller;

import cn.hutool.core.io.FileUtil;
import com.kun.blog.mapper.EmojiMapper;
import com.kun.blog.mapper.EmojiTypeMapper;
import com.kun.common.file.enums.FileSystemEnum;
import com.kun.common.file.service.FileSystemService;
import com.kun.common.http.util.OkHttpUtil;
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
    private final FileSystemService fileSystemService;


    @RequestMapping("get")
    public KunResult<?> get(Integer id) throws Exception {
//        System.out.println("id->" + id);
//        Emoji emoji = new LambdaQueryChainWrapper<>(emojiMapper)
//                .eq(Emoji::getId, id)
//                .one();
//        System.out.println(JSON.toJSONString(emoji));
//
//        EmojiType emojiType = new LambdaQueryChainWrapper<>(emojiTypeMapper)
//                .eq(EmojiType::getId, emoji.getTypeId())
//                .one();
//        System.out.println(JSON.toJSONString(emojiType));
//        return KunResult.ok(emoji);
//        String result = OkHttpUtil.doGet("测试", "http://www.baidu.com");
//        System.out.println(result);
//        String upload = fileSystemService.uploadImg(FileSystemEnum.FASTDFS, FileUtil.readBytes("E:/tupian/yuzhongmeinv.png"));
//        System.out.println(upload);
//        byte[] download = fileSystemService.download(upload);
//        if (download != null) {
//            System.out.println("下载文件的字节长度->" + download.length);
//        }
        return KunResult.ok();
    }
}
