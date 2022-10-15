package com.kun.blog.constant;

/**
 * 通用正则表达式
 *
 * @author gzc
 * @since 2022/10/16 2:58
 **/
public class RegConstant {
    /**
     * 6-20个字符，只能为字母、数字、下划线、中划线
     */
    public final static String USER_NAME_REG = "^[a-zA-Z0-9-_]{6,20}$";
    /**
     * 6-18个字符，只能为字母、数字、下划线
     */
    public final static String USER_PASS_WORD_REG = "^[a-zA-Z0-9_]{6,18}$";
}
