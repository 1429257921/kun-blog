package com.kun.common.log.enums;

import cn.hutool.core.util.StrUtil;

/**
 * 日志类型枚举
 *
 * @author: gzc
 * @createTime: 2021-12-9 11:13
 **/
public enum LogTypeEnum {
    /**
     * 日志类型
     */
    UNDEFINED_LOG(-1, "未配置日志类型"),
    SYSTEM_LOG(0, "系统日志"),
    ERROR_LOG(1, "错误日志"),
    ;

    private Integer code;
    private String description;

    LogTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Integer getCode(String description) {
        if (StrUtil.isNotBlank(description)) {
            for (LogTypeEnum value : LogTypeEnum.values()) {
                if (value.getDescription().equalsIgnoreCase(description)) {
                    return value.getCode();
                }
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
