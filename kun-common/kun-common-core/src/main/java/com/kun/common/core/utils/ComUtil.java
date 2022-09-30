package com.kun.common.core.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;

/**
 * 通用工具类
 *
 * @author: gzc
 * @createTime: 2021-11-17 18:04
 **/
@Slf4j
public class ComUtil {

    /**
     * 获取当前项目本地绝对路径
     */
    public static String projectLocalPath() {
        String localPath = System.getProperty("user.dir");
        log.info("项目本地路径为{}", localPath);
        return localPath;
    }

    /**
     * 生成简单的不带-的UUID
     */
    public static String simpleUUID() {
        return UUID.fastUUID().toString(true);
    }

    /**
     * 生成带-的UUID
     */
    public static String intricacyUUID() {
        return UUID.fastUUID().toString();
    }

    /**
     * 本地文件转Base64字符串
     *
     * @param localFilePath 本地文件全路径
     * @return 文件base64字符串
     */
    public static String fileToBase64(String localFilePath) {
        return Base64.encode(FileUtil.readBytes(localFilePath));
    }
}
