package com.kun.blog.handler;

import com.kun.common.web.handler.GlobalExceptionHandler;
import com.kun.common.web.vo.ApiError;
import com.kun.common.web.vo.KunResult;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 坤坤博客自定义全局异常处理
 *
 * @author gzc
 * @since 2022/10/6 18:06
 **/
@Component
public class KunBlogExceptionHandler extends GlobalExceptionHandler {

    public KunBlogExceptionHandler(ConfigurableEnvironment configurableEnvironment) {
        super(configurableEnvironment);
    }

    @Override
    protected Object returnAfter(HttpServletRequest request, Object result) {
        KunResult<?> kunResult = (KunResult) result;
        Integer status = Integer.valueOf(kunResult.getErrCode());
        ApiError apiError = ApiError.error(status, kunResult.getErrMsg());
        if (kunResult.getData() != null && kunResult.getData() instanceof String) {
            apiError.setMsgDetail((String) kunResult.getData());
        }
        return new ResponseEntity<>(apiError, HttpStatus.valueOf(status));
    }
}
