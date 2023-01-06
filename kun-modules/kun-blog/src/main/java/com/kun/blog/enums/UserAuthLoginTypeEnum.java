package com.kun.blog.enums;

import com.kun.blog.entity.po.UserAuth;
import lombok.Getter;

/**
 * 坤坤云用户登录授权表登录类型枚举类
 * {@link UserAuth#getLoginType()}
 *
 * @author gzc
 * @since 2023/1/5 18:16
 **/
@Getter
public enum UserAuthLoginTypeEnum {
    /**
     * 登录类型（1、手机号验证码登录 ，2、手机号一键登录， 3、账号密码登录）
     */
    PHONE_CODE(1, "手机号验证码登录"),
    PHONE_KEY(2, "手机号一键登录"),
    ACCOUNT(3, "账号密码登录"),
    ;

    private final int code;
    private final String name;

    UserAuthLoginTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(Integer code) {
        if (code != null) {
            for (UserAuthLoginTypeEnum enumItem : UserAuthLoginTypeEnum.values()) {
                if (enumItem.getCode() == code) {
                    return enumItem.getName();
                }
            }
        }
        return "";
    }
}
