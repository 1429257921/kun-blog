package com.kun.common.core.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记当前属性的值是文件数据
 *
 * @author gzc
 * @since 2022-6-15 10:10
 **/
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FlagFileData {

	String message() default "";

}
