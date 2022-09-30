package com.kun.blog.common.http.util;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * huTool 的http工具类实现http调用
 *
 * @author gzc
 * @since 2022/9/30 20:37
 */
@Slf4j
public class HuToolHttpUtil {

    /**
     * 超时时间（毫秒）
     */
    private static final Integer TIMEOUT = 15000;

    /**
     * get请求
     *
     * @param apiMsg 接口描述
     * @param url    请求地址
     * @return
     */
    public static String doGet(String apiMsg, String url) {
        return doGet(apiMsg, url, TIMEOUT);
    }

    /**
     * get请求
     *
     * @param apiMsg  接口描述
     * @param url     接口地址
     * @param timeout 超时时间(连接超时和读取超时)
     * @return
     */
    public static String doGet(String apiMsg, String url, Integer timeout) {
        log.info("调用{}, 请求路径为{}", apiMsg, url);
        String responseText = "";
        try {
            responseText = HttpUtil.get(url, timeout);
        } finally {
            log.info("调用{}返回结果为{}", apiMsg, responseText);
        }
        return responseText;
    }

}
