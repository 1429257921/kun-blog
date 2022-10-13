package com.kun.common.web.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import com.kun.common.core.exception.BizException;
import com.kun.common.core.utils.spring.ContextUtil;
import com.kun.common.web.config.KunRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取request和response工具类
 *
 * @author gzc
 * @since 2022/10/12 0:55
 */
@Slf4j
public class WebContextUtil extends ContextUtil {

    /**
     * 获取当前request的包装类(可获取请求body中的参数)
     */
    public static KunRequestWrapper getRequestWrapper() {
        return new KunRequestWrapper(getRequestAttributes().getRequest());
    }

    /**
     * 通过在接口上添加APIMessage注解，获取接口中文描述
     */
    public static String getApiMsg() {
//        String apiMsg = InitData.getApiMsg(getServletPath());
//        System.out.println(apiMsg);
//        return StrUtil.isNotBlank(apiMsg) ? apiMsg : "未知接口";
        return "";
    }

    public static String getToken() {
        return "";
    }


    /**
     * 获取请求参数(可获取URL或body中的参数,如果URL中有参数则不获取body中的参数)
     */
    public static String getReqParam() {
        StringBuilder sb = new StringBuilder();
        String urlParam = getURLParam();
        if (StrUtil.isNotBlank(urlParam)) {
            sb.append("请求URL中的参数->");
            sb.append(urlParam);
            sb.append("\n");
        }
        String bodyParam = getBodyParam();
        if (StrUtil.isNotBlank(bodyParam)) {
            sb.append("请求body中的参数->");
            sb.append(bodyParam);
        }
        String requestBody = sb.toString();
        //不为空, 移除换行符、空白符合制表符
        if (StrUtil.isNotBlank(requestBody)) {
            requestBody = requestBody.replaceAll("[\\s*\\t\\n\\r]", "");
        }
        return requestBody;
    }

    /**
     * 获取当前请求request中Body请求参数
     */
    public static String getBodyParam() {
        String requestBody = "";
        try {
            KunRequestWrapper requestWrapper = getRequestWrapper();
            if (requestWrapper != null) {
                requestBody = requestWrapper.getBody();
                if (!JSONValidator.from(requestBody).validate() && requestBody.indexOf("&") != -1) {
                    Map<String, String> map = HttpUtil.decodeParamMap(requestBody, Charset.defaultCharset());
                    requestBody = JSON.toJSONString(map);
                }
            }
        } catch (Exception e) {
            throw new BizException(getApiMsg() + "获取Body中的请求参数异常:" + e.getMessage(), e);
        }
        return requestBody;
    }

    /**
     * 获取当前请求request中URL请求参数
     */
    public static String getURLParam() {
        String requestBody;
        try {
            Map<String, String[]> parameterMap = getRequest().getParameterMap();
            requestBody = convert(parameterMap);
        } catch (Exception e) {
            throw new BizException(getApiMsg() + "获取URL中的请求参数异常:" + e.getMessage(), e);
        }
        return requestBody;
    }

    /**
     * 获取URL中的参数
     */
    private static String convert(Map<String, String[]> map) {
        String requestBody = "";
        try {
            Map<String, String> hashMap = new HashMap<>();
            // 取value数组中的第一个元素
            map.forEach((k, v) -> hashMap.put(k, ArrayUtil.isNotEmpty(v) ? v[0] : ""));
            if (CollUtil.isNotEmpty(hashMap)) {
                requestBody = JSON.toJSONString(hashMap);
            }
        } catch (Exception e) {
            log.error("URL中的请求参数获取并转map异常：{}", e);
        }
        return requestBody;
    }

}
