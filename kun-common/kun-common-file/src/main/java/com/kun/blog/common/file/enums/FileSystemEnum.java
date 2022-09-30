package com.kun.blog.common.file.enums;

import cn.hutool.core.util.StrUtil;
import com.kun.blog.common.core.exception.BizException;

/**
 * 文件服务器类型枚举
 *
 * @author gzc
 * @since 2022/9/30 20:35
 */
public enum FileSystemEnum {
    /**
     * ftp
     */
    FTP("1"),
    /**
     * fastDFS
     */
    FASTDFS("2");

    private String code;


    FileSystemEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static FileSystemEnum get(String code) throws BizException {
        if (StrUtil.isNotBlank(code)) {
            for (FileSystemEnum value : FileSystemEnum.values()) {
                if (value.getCode().equals(code)) {
                    return value;
                }
            }
        }
        throw new BizException("不匹配的文件服务器类型[" + code + "]");
    }
}
