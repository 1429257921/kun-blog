package com.kun.common.core.enums;

/**
 * 响应vo错误信息枚举
 *
 * @author gzc
 * @since 2022/9/30 20:28
 */
public enum ErrorEnum {
    // 成功段
    SUCCESS("0", "成功"),
    FAIL("-1", "失败"),
    ERROR("99", null),
    IMPOSE_ERROR("400", "操作频繁"),

    // 登录段
    LOGIN_PASSWORD_ERROR("1", "账户密码不匹配"),
    // TOKEN
    TOKEN_EXPIRE("2", "时间戳验证不通过"),
    TOKEN_EXPIRE_TOKEN("3", "登录令牌不正确"),
    TOKEN_NOT_TOKEN("9", "登录令牌不正确"),

    TOKEN_INVALID("4", "登录令牌过期，请重新获取"),

    // 参数错误
    FILES_UPLOAD("5", "上传文件验证不通过"),
    PARAM_REQUIRE("104", "请求超时"),
    PARAM_INVALID("9", "请求内部错误，请求参数不能为空"),

    FILES_ERR("9", "上传文件失败"),
    UPLOAD_FILE_MAX_ERROR("101", "上传文件大小超过限制值"),

    NETWORK_ERROR("52", "网络繁忙"),

    FILE_FTP("9", "FTP配置表配置未正确"),
    FAIL_SYSCONFIG("9", "配置表配置未正确"),
    FILE_PZ("9", "请配置上传方式"),
    STAMOSEAL("9", "调用佳禾签章平台签章接口失败"),

    OTHER_ERROR("11", "请求内部错误，参照错误描述"),

    SERVICE_REMOTE_FAIL("300", "Dubbo服务调用超时, 请稍后重试! "),
    REQUEST_METHOD_NOT_SUPPORT("415", "请求方法类型不支持"),
    SQL_EXECUTE_ERROR("400", "sql执行异常"),
    JSON_CONVERT_ERROR("422", "JSON转换错误"),
    INTERNAL_SERVER_ERROR("500", "发生服务器内部错误, 请联系管理人员!"),
    HTTP_PARAM_NOT_READ("416", "请求参数不是标准JSON字符串或参数没有JSON序列化"),

    EXPIRATION_TIME_YXQ("501", "订单表已超过有效时间"),
    NOT_SIGNSEAL_USERTYPE_YXQ("502", "请先进行企业盖章，再使用粤信签签名"),
    ;


    private String code;
    private String msg;

    ErrorEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
