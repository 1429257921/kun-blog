package com.kun.blog.entity.vo;

import com.kun.blog.security.dto.JwtUser;
import lombok.*;

import java.io.Serializable;

/**
 * 用户登录接口响应对象
 *
 * @author gzc
 * @since 2022/10/7 21:11
 **/
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginVO implements Serializable {
    /**
     * 用户token
     */
    private String token;
    /**
     * 过期时间(单位毫秒)
     */
    private Long expireTime;
    /**
     * 用户信息
     */
    private JwtUser user;

}
