package com.kun.blog.entity.vo;

import lombok.Data;

/**
 * TODO
 *
 * @author gzc
 * @since 2022/10/7 23:53
 **/
@Data
public class GetVerificationCodeVO {
    /**
     * 用户ID
     */
    private String uuid;
    /**
     * 验证码图片Base64
     */
    private String img;
}
