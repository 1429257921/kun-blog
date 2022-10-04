package com.kun.blog.entity.dto;

import lombok.Data;

/**
 * 搜索表情包接口入参
 *
 * @author gzc
 * @since 2022/10/3 21:57
 **/
@Data
public class EmojiSearchReq {
    /**
     * 当前页数
     */
    private Integer currPage;
    /**
     * 页大小
     */
    private Integer pageSize;
    /**
     * 搜索内容
     */
    private String searchContent;
}
