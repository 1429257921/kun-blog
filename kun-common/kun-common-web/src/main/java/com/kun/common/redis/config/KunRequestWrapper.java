package com.kun.common.redis.config;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 因为HttpServletRequest对象的body数据只能get，不能set，即不能再次赋值。
 * 而我们的需求是需要给HttpServletRequest赋值，
 * 所以需要定义一个HttpServletRequest实现类：MyRequestWrapper，
 * 这个实现类可以被赋值来满足我们的需求。
 *
 * @author gzc
 * @since 2022/9/30 20:44
 */
@Slf4j
public class KunRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 保存request body的数据
     */
    private String body;

    /**
     * 保存请求头信息
     */
    private Map<String, String> headerMap = new HashMap<>();

    /**
     * 解析request的inputStream(即body)数据，转成字符串
     */
    public KunRequestWrapper(HttpServletRequest request) {
        super(request);
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader;
        InputStream inputStream;
        String servletPath = request.getServletPath();
        try {
            // 获取输入流
            inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    sb.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            log.error("{}创建JiaheRequestWrapper包装类发生异常:{}", servletPath, e);
        }
        // 保存请求参数
        body = sb.toString();
    }


    /**
     * 获取request中的输入流
     */
    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() {
                return bais.read();
            }
        };
        return servletInputStream;
    }

    /**
     * 获取字符读取流
     */
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    /**
     * 添加请求头信息
     *
     * @param name
     * @param value
     */
    public void addHeader(String name, String value) {
        headerMap.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = super.getHeader(name);
        if (headerMap.containsKey(name)) {
            headerValue = headerMap.get(name);
        }
        return headerValue;
    }

    /**
     * 获取当前请求头名称集合
     *
     * @return
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        for (String name : headerMap.keySet()) {
            names.add(name);
        }
        return Collections.enumeration(names);
    }

    /**
     * 根据请求头名称获取请求头信息
     *
     * @param name
     * @return
     */
    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> values = Collections.list(super.getHeaders(name));
        if (headerMap.containsKey(name)) {
            values = Arrays.asList(headerMap.get(name));
        }
        return Collections.enumeration(values);
    }

    /**
     * 获取body
     */
    public String getBody() {
        return this.body;
    }

    /**
     * 赋值body
     */
    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}
