package com.kun.blog.enums;

import com.kun.blog.entity.po.User;
import lombok.Getter;

/**
 * 坤坤云用户表状态枚举类
 * {@link User#getStatus()}
 *
 * @author gzc
 * @since 2023/1/5 18:16
 **/
@Getter
public enum UserStatusEnum {
    /**
     * 账号状态（0、启用，1、禁用，3、注销）
     */
    ACTIVE(0, "启用"),
    STOP(1, "禁用"),
    CANCEL(2, "注销"),
    ;

    private final int code;
    private final String name;

    UserStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(Integer code) {
        if (code != null) {
            for (UserStatusEnum enumItem : UserStatusEnum.values()) {
                if (enumItem.getCode() == code) {
                    return enumItem.getName();
                }
            }
        }
        return "";
    }
}
