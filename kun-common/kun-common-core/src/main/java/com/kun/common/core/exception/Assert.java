package com.kun.common.core.exception;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 自定义断言，可抛出自定义异常BizException
 *
 * @author gzc
 * @since 2022/9/30 20:29
 */
public class Assert {

    /**
     * 检查给定字符串是否为空白（null、空串或只包含空白符），为空抛出 {@link BizException}
     *
     * @param <T>              字符串类型
     * @param text             被检查字符串
     * @param errorMsgTemplate 错误消息模板，变量使用{}表示
     * @param params           参数
     * @return 非空字符串
     */
    public static <T extends CharSequence> T notBlank(T text, String errorMsgTemplate, Object... params) {
        if (StrUtil.isBlank(text)) {
            throw new BizException(StrUtil.format(errorMsgTemplate, params));
        }
        return text;
    }

    /**
     * 检查给定字符串是否为空白（null、空串或只包含空白符），为空抛出 {@link BizException}
     *
     * @param text             被检查字符串
     * @param errorMsgTemplate 错误消息模板，变量使用{}表示
     * @param params           参数
     * @return 非空字符串
     */
    public static void isBlank(String text, String errorMsgTemplate, Object... params) {
        if (StrUtil.isNotBlank(text)) {
            throw new BizException(StrUtil.format(errorMsgTemplate, params));
        }
    }


    /**
     * 断言对象是否为{@code null} ，如果不为{@code null} 抛出{@link BizException} 异常
     *
     * @param object           被检查的对象
     * @param errorMsgTemplate 消息模板，变量使用{}表示
     * @param params           参数列表
     */
    public static void isNull(Object object, String errorMsgTemplate, Object... params) {
        if (null != object) {
            throw new BizException(StrUtil.format(errorMsgTemplate, params));
        }
    }

    /**
     * 断言对象是否不为{@code null} ，如果为{@code null} 抛出{@link BizException} 异常 Assert that an object is not {@code null} .
     *
     * @param <T>              被检查对象泛型类型
     * @param object           被检查对象
     * @param errorMsgTemplate 错误消息模板，变量使用{}表示
     * @param params           参数
     * @return 被检查后的对象
     */
    public static <T> T notNull(T object, String errorMsgTemplate, Object... params) {
        if (null == object) {
            throw new BizException(StrUtil.format(errorMsgTemplate, params));
        }
        return object;
    }

    /**
     * 断言给定集合为空
     *
     * @param collection       被检查的集合
     * @param errorMsgTemplate 异常时的消息模板
     * @param params           参数列表
     */
    public static <E, T extends Iterable<E>> T isEmpty(T collection, String errorMsgTemplate, Object... params) {
        if (CollUtil.isNotEmpty(collection)) {
            throw new BizException(StrUtil.format(errorMsgTemplate, params));
        }
        return collection;
    }

    /**
     * 断言给定集合非空
     *
     * @param <E>              集合元素类型
     * @param <T>              集合类型
     * @param collection       被检查的集合
     * @param errorMsgTemplate 异常时的消息模板
     * @param params           参数列表
     * @return 非空集合
     */
    public static <E, T extends Iterable<E>> T notEmpty(T collection, String errorMsgTemplate, Object... params) {
        if (CollUtil.isEmpty(collection)) {
            throw new BizException(StrUtil.format(errorMsgTemplate, params));
        }
        // 判断第一个元素是否为空
        for (E item : collection) {
            if (item == null) {
                throw new BizException(StrUtil.format(errorMsgTemplate, params));
            }
            break;
        }
        return collection;
    }


    /**
     * 断言给定数组是否不包含 null元素，如果数组为空或 null将被认为不包含
     *
     * @param <T>              数组元素类型
     * @param array            被检查的数组
     * @param errorMsgTemplate 异常时的消息模板
     * @param params           参数列表
     * @return 被检查的数组
     */
    public static <T> T[] notNullElements(T[] array, String errorMsgTemplate, Object... params) {
        if (ArrayUtil.hasNull(array)) {
            throw new BizException(StrUtil.format(errorMsgTemplate, params));
        }
        return array;
    }

    public static byte[] notNullElements(byte[] array, String errorMsgTemplate, Object... params) {
        if (ArrayUtil.hasNull(array)) {
            throw new BizException(StrUtil.format(errorMsgTemplate, params));
        }
        return array;
    }

    /**
     * 断言给定数组是否包含 null元素，如果数组为空或 null将被认为包含
     *
     * @param <T>              数组元素类型
     * @param array            被检查的数组
     * @param errorMsgTemplate 异常时的消息模板
     * @param params           参数列表
     * @return 被检查的数组
     */
    public static <T> T[] isNullElements(T[] array, String errorMsgTemplate, Object... params) {
        if (!ArrayUtil.hasNull(array)) {
            throw new BizException(StrUtil.format(errorMsgTemplate, params));
        }
        return array;
    }

    /**
     * 断言给定字符串是否不被另一个字符串包含（即是否为子串）
     *
     * @param textToSearch     被搜索的字符串
     * @param substring        被检查的子串
     * @param errorMsgTemplate 异常时的消息模板
     * @param params           参数列表
     * @return 被检查的子串
     */
    public static String notContain(String textToSearch, String substring, String errorMsgTemplate, Object... params) {
        if (StrUtil.contains(textToSearch, substring)) {
            throw new BizException(StrUtil.format(errorMsgTemplate, params));
        }
        return substring;
    }

    /**
     * 断言给定字符串是否被另一个字符串包含（即是否为子串）
     *
     * @param textToSearch     被搜索的字符串
     * @param substring        被检查的子串
     * @param errorMsgTemplate 异常时的消息模板
     * @param params           参数列表
     * @return 被检查的子串
     */
    public static String isContain(String textToSearch, String substring, String errorMsgTemplate, Object... params) {
        if (!StrUtil.contains(textToSearch, substring)) {
            throw new BizException(StrUtil.format(errorMsgTemplate, params));
        }
        return substring;
    }

    /**
     * 是否大于0，小于等于0则抛出自定义异常
     *
     * @param number           参数
     * @param errorMsgTemplate 错误信息模板
     * @param params           错误信息参数
     * @return: void
     * @author: gzc
     * @date: 2021-12-16 8:51
     */
    public static int moreThanZero(int number, String errorMsgTemplate, Object... params) {
        if (number <= 0) {
            throw new BizException(StrUtil.format(errorMsgTemplate, params));
        }
        return number;
    }

}
