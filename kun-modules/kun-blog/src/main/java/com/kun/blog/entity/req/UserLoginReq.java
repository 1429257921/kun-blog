package com.kun.blog.entity.req;

import com.kun.blog.enums.UserAuthLoginTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户登录接口请求参数
 *
 * @author gzc
 * @since 2022/10/7 21:08
 **/
@Data
public class UserLoginReq implements Serializable {
    /**
     * 手机号
     */
    @NotBlank(message = "手机号为空")
    private String phone;
    /**
     * 密码
     */
    private String password;
    /**
     * 登录类型（1、手机号验证码登录 ，2、手机号一键登录， 3、账号密码登录）
     * {@link UserAuthLoginTypeEnum}
     */
    @NotNull(message = "登录类型为空")
    private Integer type;
    /**
     * 手机验证码
     */
    @NotNull(message = "手机验证码为空")
    private Integer code;
    /**
     * 时间戳(毫秒)
     */
    @NotNull(message = "时间戳为空")
    private Long timestamp;
}
