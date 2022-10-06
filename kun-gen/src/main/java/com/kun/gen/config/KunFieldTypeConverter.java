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
    private static Properties properties = new Properties();

    static {
        InputStream inputStream = KunFieldTypeConverter.class
                .getClassLoader().getResourceAsStream("type-converter.properties");
        try {
            properties.load(IoUtil.getReader(inputStream, Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
        IColumnType ct = super.processTypeConvert(config, fieldType);
        String convertTypeName = null;
        for (String item : properties.stringPropertyNames()) {
            if (ct.getType().toLowerCase().contains(item)) {
                convertTypeName = properties.getProperty(item);
                break;
            }
        }
        System.out.println("mysql->" + ct.getType() + "，java->" + convertTypeName);
        if (StrUtil.isNotBlank(convertTypeName)) {
            System.out.println("==========================");
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
        System.out.println("==========================");
        return ct;
    }
}
