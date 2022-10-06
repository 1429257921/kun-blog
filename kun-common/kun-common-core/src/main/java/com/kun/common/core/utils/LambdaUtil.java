package com.kun.common.core.utils;

/**
 * lambda工具类
 *
 * @author gzc
 * @since 2022/10/6 14:40
 **/
public class LambdaUtil {

    /**
     * 获取类字段名称
     *
     * @param typeFunction
     * @param <T>
     * @param <R>
     * @return string
     */
    public static <T, R> String getFieldName(TypeFunction<T, R> typeFunction) {
        return TypeFunction.getLambdaColumnName(typeFunction);
    }
}
