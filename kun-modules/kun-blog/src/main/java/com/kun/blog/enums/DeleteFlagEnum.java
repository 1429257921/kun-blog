package com.kun.blog.enums;

import lombok.Getter;

/**
 * 删除标识枚举类
 *
 * @author gzc
 * @since 2023/1/5 18:16
 **/
@Getter
public enum DeleteFlagEnum {
    /**
     * 删除标志（0、正常，1、删除）
     */
    ACTIVE(0, "正常"),
    STOP(1, "删除"),
    ;

    private final int code;
    private final String name;

    DeleteFlagEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(Integer code) {
        if (code != null) {
            for (DeleteFlagEnum enumItem : DeleteFlagEnum.values()) {
                if (enumItem.getCode() == code) {
                    return enumItem.getName();
                }
            }
        }
        return "";
    }
}
