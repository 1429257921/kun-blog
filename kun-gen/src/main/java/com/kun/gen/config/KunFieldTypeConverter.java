package com.kun.gen.config;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * 表字段与实体类属性字段自定义映射
 *
 * @author gzc
 * @since 2022/10/6 16:06
 **/
public class KunFieldTypeConverter extends MySqlTypeConvert {
    private static final Properties PROPERTIES = new Properties();

    static {
        InputStream inputStream = KunFieldTypeConverter.class
                .getClassLoader().getResourceAsStream("type-converter.properties");
        try {
            PROPERTIES.load(IoUtil.getReader(inputStream, Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
        IColumnType ct = super.processTypeConvert(config, fieldType);
        String convertTypeName = null;
        // 第一次全字符匹配
        for (String item : PROPERTIES.stringPropertyNames()) {
            if (fieldType.toLowerCase().equalsIgnoreCase(item)) {
                convertTypeName = PROPERTIES.getProperty(item);
                break;
            }
        }
        //第一次全字符匹配未匹配上时，进行第二次模糊匹配
        if (StrUtil.isBlank(convertTypeName)) {
            for (String item : PROPERTIES.stringPropertyNames()) {
                if (fieldType.toLowerCase().contains(item)) {
                    convertTypeName = PROPERTIES.getProperty(item);
                    break;
                }
            }
        }

        System.out.println("mysql类型->" + fieldType + "，java类型->" + convertTypeName);
        if (StrUtil.isNotBlank(convertTypeName)) {
            String finalConvertTypeName = convertTypeName;
            return new IColumnType() {
                @Override
                public String getType() {
                    // 你想修改的类型
                    return finalConvertTypeName;
                }

                @Override
                public String getPkg() {
                    // 类型所在包
                    return null;
                }
            };
        }
        return ct;
    }
}
