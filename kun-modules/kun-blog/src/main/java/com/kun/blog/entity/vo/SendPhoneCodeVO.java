package com.kun.blog.entity.vo;

import lombok.*;

/**
 * 发送手机短信验证码接口响应VO
 *
 * @author gzc
 * @since 2023/1/6 11:43
 **/
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendPhoneCodeVO {
    /**
     * 手机短信验证码
     */
    private Integer code;
}
