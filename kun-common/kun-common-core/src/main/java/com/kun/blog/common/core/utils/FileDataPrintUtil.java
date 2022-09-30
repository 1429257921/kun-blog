package com.kun.blog.common.core.utils;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.kun.blog.common.core.anno.FlagFileData;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * 过滤对象中的文件数据，转化成json字符串 工具类
 *
 * @author gzc
 * @since 2022/9/30 20:32
 */
@Slf4j
public class FileDataPrintUtil {

    /**
     * 过滤对象中的文件数据
     *
     * @param obj 对象
     * @return {@link String} 过滤后的JSON字符串
     */
    public static String toStringFilterFileStr(Object obj) {
        try {
            return JSON.toJSONString(toStringFilterFile(obj));
        } catch (Exception e) {
            log.error("日志输出请求参数发生异常,异常原因:{}", e);
        }
        return "";
    }

    /**
     * 过滤对象中的文件数据
     *
     * @param obj 对象
     * @return {@link Object} 过滤后的Object对象
     */
    public static Object toStringFilterFile(Object obj) {
        if (obj != null && JSONValidator.from(obj.toString()).validate()) {
            JSONValidator.Type type = JSONValidator.from(obj.toString()).getType();
            // 判断属性是否是对象
            if (type != null && JSONValidator.Type.Object.name().equals(type.name())) {
                JSONObject jsonObj = JSON.parseObject(obj.toString());
                // 遍历当前对象所有属性
                for (Field field : ReflectUtil.getFields(obj.getClass())) {
                    // 获取当前属性的值
                    Object fieldValue = ReflectUtil.getFieldValue(obj, field);
                    // 当前属性是否带有标记注解
                    FlagFileData annotation = field.getAnnotation(FlagFileData.class);
                    if (annotation != null) {
                        String message = annotation.message();
                        if (fieldValue instanceof byte[]) {
                            jsonObj.put(field.getName(),
                                    (StrUtil.isBlank(message) ? "字节长度->" : message) + (((byte[]) fieldValue).length));
                        } else if (fieldValue instanceof String) {
                            jsonObj.put(field.getName(),
                                    (StrUtil.isBlank(message) ? "字符串长度->" : message) + (((String) fieldValue).length()));
                        }
                    } else {
                        // 递归
                        jsonObj.put(field.getName(), toStringFilterFile(fieldValue));
                    }
                }
                return jsonObj;
            }
        }
        return obj;
    }

}
