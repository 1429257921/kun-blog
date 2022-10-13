package com.kun.blog.entity.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 校验验证码接口请求参数
 *
 * @author gzc
 * @since 2022/10/13 3:29
 **/
@Data
public class ValidatedCodeReq {
    /**
     * id
     */
    @NotBlank(message = "uuid为空")
    private String uuid;
    /**
     * 验证码
     */
    @NotBlank(message = "验证码为空")
    private String code;
}
