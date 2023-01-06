package com.kun.blog.enums;

import com.kun.blog.entity.po.User;
import lombok.Getter;

/**
 * 坤坤云用户表性别枚举类
 * {@link User#getSex()}
 *
 * @author gzc
 * @since 2023/1/5 18:16
 **/
@Getter
public enum UserSexEnum {
    /**
     * 性别（0、男，1、女）
     */
    MAN(0, "男"),
    WOMAN(1, "女"),
    ;

    private final int code;
    private final String name;

    UserSexEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(Integer code) {
        if (code != null) {
            for (UserSexEnum enumItem : UserSexEnum.values()) {
                if (enumItem.getCode() == code) {
                    return enumItem.getName();
                }
            }
        }
        return "";
    }
}
