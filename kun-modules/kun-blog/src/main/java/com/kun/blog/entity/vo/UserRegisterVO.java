package com.kun.blog.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册接口响应对象
 *
 * @author gzc
 * @since 2022/10/16 1:00
 **/
@Data
public class UserRegisterVO implements Serializable {
    /**
     * jwt秘钥
     */
    private String token;
}
