package com.kun.blog.common.core.constants;

/**
 * 通用正则表达式
 *
 * @author gzc
 * @since 2022/9/30 20:28
 */

public class ComRegexConstant {

    /**
     * 移除特殊字符
     */
//	public static final String SPECIAL_CHARACTERS = "\\pP|\\pS|\\s+";
    /**
     * 去除制表符、空格符和换行符
     */
    public static final String SPECIAL_CHARACTERS = "\\s*|\t|\r|\n";

}
