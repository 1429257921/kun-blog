package com.kun.common.log.util;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.kun.common.core.utils.spring.ContextUtil;
import com.kun.common.log.anno.APIMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 初始化数据
 *
 * @author gzc
 * @since 2022/10/12 2:48
 **/
@Slf4j
@RequiredArgsConstructor
public class InitData implements CommandLineRunner {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    private static final ConcurrentMap<String, String> STATIC_MAP;

    static {
        STATIC_MAP = new ConcurrentHashMap<>(32);
    }

    @Override
    public void run(String... args) throws Exception {
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap =
                requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            Set<PathPattern> patterns = infoEntry.getKey().getPathPatternsCondition().getPatterns();
            if (CollUtil.isNotEmpty(patterns)) {
                for (PathPattern pattern : patterns) {
                    if (pattern != null) {
                        buildApiMsg(handlerMethod, pattern.getPatternString());
                    }
                }
            }
        }
        System.out.println(JSON.toJSONString(STATIC_MAP));
    }

    /**
     * 构建接口和接口名称映射
     *
     * @param handlerMethod
     */
    private void buildApiMsg(HandlerMethod handlerMethod, String url) {
        APIMessage apiMessage = handlerMethod.getMethodAnnotation(APIMessage.class);
        if (apiMessage != null) {
            String apiMsg = "[" + apiMessage.value() + "]接口";
            STATIC_MAP.put(url, apiMsg);
        }
    }

    /**
     * 获取接口中文描述
     *
     * @param url 示例 /api/auth/login
     * @return 接口中文描述
     */
    public static String getApiMsg(String url) {
        return STATIC_MAP.get(url);
    }

    /**
     * 获取接口中文描述
     *
     * @return 接口中文描述
     */
    public static String getApiMsg() {
        return STATIC_MAP.get(ContextUtil.getRequest().getServletPath());
    }
}
