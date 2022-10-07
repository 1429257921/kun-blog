package com.kun.blog.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * TODO
 *
 * @author gzc
 * @since 2022/10/7 13:52
 **/
@Data
public class EmojiGetConfigReq {
    /**
     * 用户ID
     */
    @NotBlank(message = "用户ID为空")
    private String userId;

    /**
     * 用户别名
     */
    private String alias;
}
