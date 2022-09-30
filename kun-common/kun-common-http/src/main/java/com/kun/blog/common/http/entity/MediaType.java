package com.kun.blog.common.http.entity;

import lombok.Getter;

/**
 * 媒体类型
 *
 * @author gzc
 * @since 2022/9/30 20:37
 */
@Getter
public enum MediaType {
    /**
     * 媒体类型
     */
    PNG_MEDIA_TYPE("image/png; charset=utf-8"),
    PDF_MEDIA_TYPE("application/pdf; charset=utf-8"),
    ;


    private String value;

    MediaType(String value) {
        this.value = value;
    }
}
