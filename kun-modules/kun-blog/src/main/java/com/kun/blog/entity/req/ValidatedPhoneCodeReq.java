package com.kun.blog.entity.req;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO
 *
 * @author gzc
 * @since 2023/1/5 20:23
 **/
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ValidatedPhoneCodeReq {
    /**
     * 手机号
     */
    @NotBlank(message = "手机号为空")
    private String phone;
    /**
     * 手机短信验证码
     */
    @NotNull(message = "手机短信验证码为空")
    private Integer code;
}
