package com.kun.blog.entity.vo;

import lombok.Data;

/**
 * 用户登录接口响应对象
 *
 * @author gzc
 * @since 2022/10/7 21:11
 **/
@Data
public class UserLoginVO {
    /**
     * 用户token
     */
    private String userToken;

}
