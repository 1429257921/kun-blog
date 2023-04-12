package com.kun.blog.enums;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.Getter;

/**
 * 敏感词影响方式枚举类
 *
 * @author gzc
 * @since 2023/2/13 12:11
 **/
@Getter
public enum SensitiveWordModeEnum implements IEnum<Integer> {
    /**
     * 影响方式（1、屏蔽，2、脱敏，3、警告）
     */
    SHIELD(1, "屏蔽"),
    DST(2, "脱敏"),
    WARN(3, "警告");

    @EnumValue
    @JsonValue
    private final Integer value;

    private final String name;

    SensitiveWordModeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static SensitiveWordModeEnum getName(Integer value) {
        if (value != null) {
            for (SensitiveWordModeEnum valueEnum : SensitiveWordModeEnum.values()) {
                if (valueEnum.getValue().equals(value)) {
                    return valueEnum;
                }
            }
        }
        return null;
    }

    public static SensitiveWordModeEnum getValue(String name) {
        if (StrUtil.isNotBlank(name)) {
            for (SensitiveWordModeEnum valueEnum : SensitiveWordModeEnum.values()) {
                if (valueEnum.getName().equals(name)) {
                    return valueEnum;
                }
            }
        }
        return null;
    }
}
