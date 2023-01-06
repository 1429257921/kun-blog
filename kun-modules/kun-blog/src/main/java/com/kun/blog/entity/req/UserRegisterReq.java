package com.kun.blog.entity.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户注册接口请求参数
 *
 * @author gzc
 * @since 2022/10/7 21:08
 **/
@Data
public class UserRegisterReq implements Serializable {
    /**
     * 手机号
     */
    @NotBlank(message = "手机号为空")
    private String phone;
    /**
     * 手机验证码
     */
    @NotNull(message = "手机验证码为空")
    private Integer code;
    /**
     * 时间戳(秒)
     */
    @NotNull(message = "时间戳为空")
    private Long timestamp;
}
