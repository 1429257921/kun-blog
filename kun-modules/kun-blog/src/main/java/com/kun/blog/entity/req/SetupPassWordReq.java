package com.kun.blog.entity.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 设置密码接口请求参数
 *
 * @author gzc
 * @since 2023/1/6 14:33
 **/
@Data
public class SetupPassWordReq {

    /**
     * 验证码
     */
    @NotNull(message = "验证码为空")
    private Integer code;
    /**
     * 密码
     */
    @NotBlank(message = "密码为空")
    private String password;
}
