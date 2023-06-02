package com.kun.common.database.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kun.common.database.anno.Query;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * mybatis-plus查询工具类
 *
 * @author gzc
 * @since 2022/10/6 14:27
 */
@Slf4j
public class MpQueryUtil {

    /**
     * 获取查询包装器
     *
     * @param query 查询请求参数
     * @param cls   查询表的实体类字节码对象
     * @return 查询包装器
     */
    @SuppressWarnings("ALL")
    public static <Q, R> QueryWrapper<R> getPredicate(Q query, Class<R> cls) {
        QueryWrapper<R> queryWrapper = new QueryWrapper<>();
        if (query == null) {
            return queryWrapper;
        }
        try {
            // 获取所有属性
            List<Field> fieldList = getAllFieldList(query.getClass(), new ArrayList<>(16));
            for (Field field : fieldList) {
                field.setAccessible(true);
                // 获取属性上的查询注解
                Query anno = field.getAnnotation(Query.class);
                if (anno != null) {
                    String propName = anno.propName();
                    String blurry = anno.blurry();
                    Query.Type type = anno.type();
                    // 数据库表的列名称
                    String columnName;
                    if (StrUtil.isBlank(propName)) {
                        // 大写字母转换成_
                        columnName = humpToUnderline(field.getName());
                    } else {
                        columnName = propName;
                    }
                    //  数据库表的列值
                    Object columnValue = field.get(query);
                    if (ObjectUtil.isEmpty(columnValue)) {
                        continue;
                    }
                    // 模糊多字段
                    if (ObjectUtil.isNotEmpty(blurry)) {
                        String[] blurryArr = blurry.split(StrPool.COMMA);
                        queryWrapper.and(wrapper -> {
                            for (String item : blurryArr) {
                                String column = humpToUnderline(item);
                                wrapper.or();
                                wrapper.like(column, columnValue.toString());
                            }
                        });
                        continue;
                    }
                    // 构建其他查询条件
                    doBuildOtherQueryWrapper(columnName, columnValue, type, queryWrapper);
                }
            }
        } catch (Exception e) {
            log.error("构建mp查询包装器发生异常, 异常原因", e);
        }
        return queryWrapper;
    }

    /**
     * 构建其他查询条件
     *
     * @param columnName   列名称
     * @param columnValue  列值
     * @param queryType    查询条件类型
     * @param queryWrapper 查询包装器对象
     * @param <Q>          查询表的实体类
     * @throws ParseException
     */
    @SuppressWarnings("all")
    private static <Q> void doBuildOtherQueryWrapper(String columnName,
                                                     Object columnValue,
                                                     Query.Type queryType,
                                                     QueryWrapper<Q> queryWrapper) throws ParseException {
        switch (queryType) {
            case EQUAL:
                queryWrapper.eq(columnName, columnValue);
                break;
            case GREATER_THAN:
                queryWrapper.ge(columnName, columnValue);
                break;
            case LESS_THAN:
                queryWrapper.le(columnName, columnValue);
                break;
            case LESS_THAN_NQ:
                queryWrapper.lt(columnName, columnValue);
                break;
            case INNER_LIKE:
                queryWrapper.like(columnName, columnValue);
                break;
            case LEFT_LIKE:
                queryWrapper.likeLeft(columnName, columnValue);
                break;
            case RIGHT_LIKE:
                queryWrapper.likeRight(columnName, columnValue);
                break;
            case IN:
                if (columnValue instanceof Collection) {
                    Collection list = (Collection) columnValue;
                    if (CollUtil.isNotEmpty(list)) {
                        queryWrapper.in(columnName, list);
                    }
                }
                break;
            case NOT_EQUAL:
                queryWrapper.ne(columnName, columnValue);
                break;
            case NOT_NULL:
                queryWrapper.isNotNull(columnName);
                break;
            case BETWEEN:
                if (columnValue instanceof List) {
                    List<Object> list = (List) columnValue;
                    if (CollUtil.isNotEmpty(list) && list.size() > 1) {
                        queryWrapper.between(columnName, list.get(0), list.get(1));
                    }
                }
                if (columnValue instanceof Array) {
                    Object[] arr = (Object[]) columnValue;
                    if (ArrayUtil.isNotEmpty(arr) && arr.length > 1) {
                        queryWrapper.between(columnName, arr[0], arr[1]);
                    }
                }
                break;
            case UNIX_TIME_STAMP:
                List<Object> list = (List) columnValue;
                if (CollUtil.isNotEmpty(list) && list.size() > 1) {
                    SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    long time1 = fm.parse(list.get(0).toString()).getTime() / 1000;
                    long time2 = fm.parse(list.get(1).toString()).getTime() / 1000;
                    queryWrapper.between(columnName, time1, time2);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 递归获取所有属性对象
     *
     * @param clazz     字节码对象
     * @param fieldList 属性集合
     * @return 属性集合
     */
    private static List<Field> getAllFieldList(Class<?> clazz, List<Field> fieldList) {
        if (clazz != null) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            // 递归
            getAllFieldList(clazz.getSuperclass(), fieldList);
        }
        return fieldList;
    }

    /***
     * 驼峰命名转为下划线命名
     *
     * @param param 驼峰命名的字符串
     * @return 大写转_后的字符串
     */
    public static String humpToUnderline(String param) {
        StringBuilder sb = new StringBuilder(param);
        // 定位
        int temp = 0;
        // 查看属性名称是否带有_
        if (!param.contains(StrPool.UNDERLINE)) {
            for (int i = 0; i < param.length(); i++) {
                // 是否是大写字母
                if (Character.isUpperCase(param.charAt(i))) {
                    sb.insert(i + temp, StrPool.UNDERLINE);
                    temp += 1;
                }
            }
        }
        return sb.toString();
    }


}
