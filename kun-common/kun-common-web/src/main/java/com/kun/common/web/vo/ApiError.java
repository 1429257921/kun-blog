package com.kun.common.web.vo;

import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.util.Date;

/**
 * 错误响应VO
 *
 * @author gzc
 * @since 2022/10/6 19:20
 */
@Data
public class ApiError {

    private Integer status = 400;
    private Date timestamp;
    private String message;
    private String msgDetail;

    private ApiError() {
        timestamp = DateUtil.date();
    }

    public static ApiError error(String message) {
        ApiError apiError = new ApiError();
        apiError.setMessage(message);
        return apiError;
    }

    public static ApiError error(String message, String msgDetail) {
        ApiError apiError = new ApiError();
        apiError.setMessage(message);
        apiError.setMsgDetail(msgDetail);
        return apiError;
    }

    public static ApiError error(Integer status, String message) {
        ApiError apiError = new ApiError();
        apiError.setStatus(status);
        apiError.setMessage(message);
        return apiError;
    }

    public static ApiError error(Integer status, String message, String msgDetail) {
        ApiError apiError = new ApiError();
        apiError.setStatus(status);
        apiError.setMessage(message);
        apiError.setMsgDetail(msgDetail);
        return apiError;
    }
}


