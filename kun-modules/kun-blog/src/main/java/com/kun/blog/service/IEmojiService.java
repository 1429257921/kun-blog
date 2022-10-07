package com.kun.blog.service;

import com.github.pagehelper.PageInfo;
import com.kun.blog.entity.dto.EmojiGetConfigReq;
import com.kun.blog.entity.dto.EmojiSearchReq;
import com.kun.blog.entity.po.Emoji;
import com.kun.blog.entity.vo.EmojiGetConfigVO;
import com.kun.common.database.service.BaseService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 坤坤表情包业务接口
 *
 * @author gzc
 * @since 2022-10-01 18:49:43
 */
public interface IEmojiService extends BaseService<Emoji> {

    /**
     * 搜索表情包
     *
     * @param emojiSearchReq
     * @param pageable
     * @return
     */
    PageInfo<Emoji> search(EmojiSearchReq emojiSearchReq, Pageable pageable);

    /**
     * 获取配置信息
     *
     * @param emojiSearchReq
     * @return
     */
    EmojiGetConfigVO getConfig(EmojiGetConfigReq emojiSearchReq);

    /**
     * 增加表情包
     *
     * @param file
     * @return
     */
    Object upload(MultipartFile file) throws IOException;
}
