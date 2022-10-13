package com.kun.common.web.config;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 自定义过滤器
 *
 * @author gzc
 * @since 2022/9/30 20:43
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@Component
public class GlobalHandlerFilter implements Filter {

    /**
     * 将请求放入请求链中
     * 创建一个入口，在这个入口中定义一个机会：
     * 将我们自定义的RSAHttpServletRequestWrapper代替HttpServletRequest随着请求传递下去
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            String contentType = req.getContentType();
            //文件上传类型
            if (StrUtil.isNotBlank(contentType) && contentType.startsWith("multipart/")) {
                chain.doFilter(request, response);
                return;
            }

            if (request instanceof HttpServletRequest) {
                KunRequestWrapper rw = new KunRequestWrapper(req);
                if (rw != null) {
                    System.out.println(rw.getBody());
                    chain.doFilter(rw, response);
                    return;
                }
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("将请求放入请求链中异常:{}", e);
        }
    }

}
