package com.kun.blog.common.redis.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.kun.blog.common.core.enums.ErrorEnum;
import com.kun.blog.common.core.exception.BizException;
import com.kun.blog.common.redis.entity.vo.KunResult;

/**
 * 响应VO工具类
 *
 * @author gzc
 * @since 2022/9/30 20:45
 */
public class KunResultUtil {

    public static KunResult ok() {
        return new KunResult(ErrorEnum.SUCCESS.getCode(), ErrorEnum.SUCCESS.getMsg(), null);
    }

    public static KunResult ok(Object data) {
        return new KunResult(ErrorEnum.SUCCESS.getCode(), ErrorEnum.SUCCESS.getMsg(), data);
    }

    public static KunResult okMsg(String okMsg) {
        return new KunResult(ErrorEnum.SUCCESS.getCode(), okMsg, null);
    }

    public static KunResult err(ErrorEnum errorEnum) {
        return new KunResult(errorEnum.getCode(), errorEnum.getMsg(), null);
    }

    public static KunResult errDetailMsg(ErrorEnum errorEnum, String data) {
        return new KunResult(errorEnum.getCode(), errorEnum.getMsg(), data);
    }

    public static KunResult errDetailMsg(ErrorEnum errorEnum, Exception e) {
        return new KunResult(errorEnum.getCode(), errorEnum.getMsg(), ExceptionUtil.stacktraceToString(e));
    }

    public static KunResult err(String errMsg) {
        return new KunResult(ErrorEnum.ERROR.getCode(), errMsg, null);
    }

    public static KunResult err(String errMsg, String errDetailMsg) {
        return new KunResult(ErrorEnum.ERROR.getCode(), errMsg, errDetailMsg);
    }

    public static KunResult err(BizException e) {
        return new KunResult(e.getErrCode(), e.getErrMsg(), null);
    }

    public static KunResult errDetail(BizException e) {
        return new KunResult(e.getErrCode(), e.getErrMsg(), ExceptionUtil.stacktraceToString(e));
    }
}
