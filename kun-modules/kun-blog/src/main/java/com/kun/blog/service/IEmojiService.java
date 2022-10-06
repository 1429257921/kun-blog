package com.kun.blog.service;

import com.github.pagehelper.PageInfo;
import com.kun.blog.entity.dto.EmojiSearchReq;
import com.kun.blog.entity.po.Emoji;
import com.kun.common.database.service.BaseService;
import org.springframework.data.domain.Pageable;

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
}
