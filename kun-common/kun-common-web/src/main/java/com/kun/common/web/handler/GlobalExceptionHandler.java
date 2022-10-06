package com.kun.common.web.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import com.alibaba.fastjson.JSONException;
import com.kun.common.web.util.WebContextUtil;
import com.kun.common.core.enums.ErrorEnum;
import com.kun.common.core.exception.BizException;
import com.kun.common.core.utils.ThreadLocalUtil;
import com.kun.common.web.vo.KunResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.net.SocketException;
import java.sql.SQLRecoverableException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 *
 * @author gzc
 * @since 2022/9/30 20:44
 */
@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private final ConfigurableEnvironment configurableEnvironment;

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = BizException.class)
    public Object bizExceptionHandler(HttpServletRequest req, BizException e) {
        String url = req.getServletPath();
        log.error("{}发生业务异常, 接口路径{}, 异常原因:{}",
                WebContextUtil.getApiMsg(), url, e);
        log.info("{}发生业务异常, 接口路径{}, 异常原因简单描述为:{}\n",
                WebContextUtil.getApiMsg(), url, e.getErrMsg());

        KunResult kunResult = KunResult.errDetail(e);
        return returnAfter(req, kunResult);
    }

//    /**
//     * 处理Dubbo RPC调用异常
//     */
//    @ExceptionHandler(value = RpcException.class)
//    public Object rpcExceptionHandler(HttpServletRequest req, RpcException e) {
//        String url = req.getServletPath();
//        log.error("{}发生服务异常, 接口路径{}, 异常原因:{}",
//                WebContextUtil.getApiMsg(), url, e);
//        log.info("{}发生服务异常, 接口路径{}, 异常原因简单描述为:{}\n",
//                WebContextUtil.getApiMsg(), url, e.getMessage());
//
//        BaseResult baseResult = BaseResult.errDetailMsg(ErrorEnum.SERVICE_REMOTE_FAIL, e);
//        return returnAfter(req, baseResult);
//    }

    /**
     * 处理请求方法类型不支持异常
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Object httpRequestMethodNotSupportedExceptionHandler(
            HttpServletRequest req, HttpRequestMethodNotSupportedException e) {
        String url = req.getServletPath();
        log.error("{}发生请求方法不支持异常,接口路径{}, 异常原因:{}",
                WebContextUtil.getApiMsg(), url, e);
        KunResult kunResult = KunResult.errDetailMsg(ErrorEnum.REQUEST_METHOD_NOT_SUPPORT, e);
        return returnAfter(req, kunResult);
    }

    /**
     * 接口入参参数校验异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Object methodArgumentNotValidExceptionHandler(HttpServletRequest req,
                                                         MethodArgumentNotValidException e) {
        List<String> violations = e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).
                collect(Collectors.toList());
        String errMsg = CollUtil.join(violations, String.valueOf(CharUtil.COMMA));
        String url = req.getServletPath();
        log.error("{}发生参数校验异常, 接口路径{}, 异常原因:{}",
                WebContextUtil.getApiMsg(), url, e);
        log.info("{}发生参数校验异常, 接口路径{}, 异常原因简单描述为:{}\n",
                WebContextUtil.getApiMsg(), url, errMsg);
        KunResult kunResult = KunResult.err(errMsg);
        return returnAfter(req, kunResult);
    }

    /**
     * 文件上传大小超过限制值
     */
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public Object maxUploadSizeExceededExceptionHandler(HttpServletRequest req,
                                                        MaxUploadSizeExceededException e) {
        String url = req.getServletPath();
        // 30MB
        String property = configurableEnvironment.getProperty("spring.servlet.multipart.max-file-size");
        log.error("{}发生上传的文件大小超过限制值{}, 接口路径{}, 异常原因:{}", WebContextUtil.getApiMsg(), property, url, e);
        log.info("{}发生上传的文件大小超过限制值{}, 接口路径{}", WebContextUtil.getApiMsg(), property, url);

        KunResult result = KunResult.err(ErrorEnum.UPLOAD_FILE_MAX_ERROR);
        result.setErrMsg(result.getErrMsg() + property);
        return returnAfter(req, result);
    }

    /**
     * 处理sql异常
     */
    @ExceptionHandler({SQLRecoverableException.class})
    public Object handleSQLRecoverableException(HttpServletRequest req, SQLRecoverableException e) {
        String url = req.getServletPath();
        log.error("{}发生sql执行异常, 接口路径{}, 异常原因:{}",
                WebContextUtil.getApiMsg(), url, e);
        log.info("{}发生sql执行异常, 接口路径{}, 异常简单描述为:{}\n",
                WebContextUtil.getApiMsg(), url, e.getMessage());

        KunResult kunResult = KunResult.errDetailMsg(ErrorEnum.SQL_EXECUTE_ERROR, e);
        return returnAfter(req, kunResult);
    }

    /**
     * 处理JSON异常
     */
    @ExceptionHandler({JSONException.class})
    public Object handleJSONExceptionException(HttpServletRequest req, JSONException e) {
        String url = req.getServletPath();
        log.error("{}发生JSON异常, 接口路径{}, 异常原因:{}",
                WebContextUtil.getApiMsg(), url, e);
        log.info("{}发生JSON异常, 接口路径{}, 异常简单描述为:{}\n",
                WebContextUtil.getApiMsg(), url, e.getMessage());

        KunResult kunResult = KunResult.errDetailMsg(ErrorEnum.JSON_CONVERT_ERROR, e);
        return returnAfter(req, kunResult);
    }

    /**
     * 处理网络异常
     */
    @ExceptionHandler(value = {SocketException.class, TimeoutException.class})
    public Object netErrHandler(HttpServletRequest req, Exception e) {
        String url = req.getServletPath();
        log.error("{}发生网络异常, 接口路径{}, 异常原因:{}",
                WebContextUtil.getApiMsg(), url, e);
        log.info("{}发生网络异常, 接口路径{}, 异常简单描述为:{}\n",
                WebContextUtil.getApiMsg(), url, e.getMessage());

        KunResult kunResult = KunResult.errDetailMsg(ErrorEnum.NETWORK_ERROR, e);
        return returnAfter(req, kunResult);
    }

    /**
     * 处理未知异常
     */
    @ExceptionHandler(value = Exception.class)
    public Object exceptionHandler(HttpServletRequest req, Exception e) {
        String url = req.getServletPath();
        log.error("{}发生未知异常, 接口路径为{}, 异常原因:{}",
                WebContextUtil.getApiMsg(), url, e);
        log.info("{}发生未知异常, 接口路径为{}, 异常简单描述为:{}\n",
                WebContextUtil.getApiMsg(), url, e.getMessage());

        KunResult kunResult = KunResult.errDetailMsg(ErrorEnum.INTERNAL_SERVER_ERROR, e);
        return returnAfter(req, kunResult);
    }


    /**
     * 返回前的操作方法
     *
     * @param request
     * @param result
     */
    protected Object returnAfter(HttpServletRequest request, Object result) {
        ThreadLocalUtil.remove();
        return result;
    }
}
