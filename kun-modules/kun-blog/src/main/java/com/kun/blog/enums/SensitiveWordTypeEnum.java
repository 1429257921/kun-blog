package com.kun.blog.enums;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 敏感词类型枚举类
 *
 * @author gzc
 * @since 2023/2/13 17:33
 **/
@Getter
public enum SensitiveWordTypeEnum implements IEnum<Integer> {
    /**
     * 敏感词类型
     */
    ILLEGAL(0,"违规文本"),
    SPAM(1, "文字垃圾内容识别"),
    POLITICS(2, "文字敏感内容识别"),
    ABUSE(3, "文字辱骂内容识别"),
    TERRORISM(4, "文字暴恐内容识别"),
    PORN(5, "文字鉴黄内容识别"),
    FLOOD(6, "文字灌水内容识别"),
    CONTRABAND(7, "文字违禁内容识别"),
    AD(8, "文字广告内容识别"),

    ;
    @EnumValue
    @JsonValue
    private final Integer value;

    private final String name;

    SensitiveWordTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static SensitiveWordTypeEnum getName(Integer value) {
        if (value != null) {
            for (SensitiveWordTypeEnum valueEnum : SensitiveWordTypeEnum.values()) {
                if (valueEnum.getValue().equals(value)) {
                    return valueEnum;
                }
            }
        }
        return null;
    }

    public static SensitiveWordTypeEnum getValue(String name) {
        if (StrUtil.isNotBlank(name)) {
            for (SensitiveWordTypeEnum valueEnum : SensitiveWordTypeEnum.values()) {
                if (valueEnum.getName().equals(name)) {
                    return valueEnum;
                }
            }
        }
        return null;
    }
}
