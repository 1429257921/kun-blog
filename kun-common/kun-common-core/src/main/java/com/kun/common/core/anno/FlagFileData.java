package com.kun.common.core.anno;

import com.kun.common.core.utils.FileDataPrintUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记当前属性的值是文件数据, 配合{@link FileDataPrintUtil} 使用
 *
 * @author gzc
 * @since 2022/9/30 20:27
 */

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FlagFileData {

    String message() default "";

}
