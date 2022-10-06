package com.kun.blog.entity.dto;

import com.kun.common.database.anno.Query;
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
     * 搜索内容
     */
    @Query(type = Query.Type.EQUAL, propName = "name")
    private String searchContent;
}
