package com.kun.blog.entity.req;

import com.kun.common.database.anno.Query;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录接口请求参数
 *
 * @author gzc
 * @since 2022/10/7 21:08
 **/
@Data
public class UserLoginReq {
    /**
     * 用户名
     */
    @Query(propName = "username")
    @NotBlank(message = "用户名为空")
    private String userName;
    /**
     * 密码
     */
    @Query(propName = "password")
    @NotBlank(message = "密码为空")
    private String passWord;
    /**
     * 验证码
     */
    private String code;
    /**
     * 时间戳
     */
    private String timestamp;
}
