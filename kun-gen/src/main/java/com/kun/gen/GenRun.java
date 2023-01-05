package com.kun.gen;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.kun.gen.config.KunFieldTypeConverter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Properties;

/**
 * 代码生成
 * 注意： 请先修改resources目录下的generator.properties文件中的配置，无误则执行当前类下的main方法
 *
 * @author gzc
 * @since 2022/10/1 11:09
 **/
@SuppressWarnings("all")
public class GenRun {
    /**
     * 代码生成入口
     * 注意： 请先修改resources目录下的generator.properties文件中的配置
     */
    public static void main(String[] args) {
        getPath();
        doGenerator();
    }


    private static Properties properties = new Properties();

    static {
        // 读取resources目录下的配置文件
        InputStream inputStream = GenRun.class.getClassLoader().getResourceAsStream("generator.properties");
        try {
            properties.load(IoUtil.getReader(inputStream, Charset.defaultCharset()));
            // 处理配置信息
            handlerProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 代码生成实现
     */
    private static void doGenerator() {
        // 构建代码生成器类
        AutoGenerator mpg = buildGenerator();
        // 构建全局配置类
        GlobalConfig globalConfig = buildGlobalConfig();
        // 构建包配置类
        PackageConfig packageConfig = buildPackageConfig();
        // 构建模板配置类
        TemplateConfig templateConfig = buildTemplateConfig();
        // 构建策略配置类
        StrategyConfig strategyConfig = buildStrategyConfig();
        // 添加配置类
        mpg.global(globalConfig);
        mpg.packageInfo(packageConfig);
        mpg.template(templateConfig);
        mpg.strategy(strategyConfig);
        // 开始生成代码文件
        mpg.execute(new FreemarkerTemplateEngine());
    }

    /**
     * 处理配置信息
     */
    private static void handlerProperties() {
        System.out.println("读取的配置内容如下:");
        if (properties != null) {
            // 遍历
            properties.forEach((k, v) -> {
                // 字符串首尾去空
                if (v instanceof String) {
                    String v2 = ((String) v).trim();
                    if (k instanceof String) {
                        properties.setProperty((String) k, v2);
                    }
                }
            });
        }
        System.out.println(JSONUtil.toJsonPrettyStr(properties));
        System.out.println("==================================");
    }

    /**
     * 构建模板配置类
     *
     * @return TemplateConfig
     */
    private static TemplateConfig buildTemplateConfig() {
        String absolutePath = File.separator + "templates";
        String entityTempPath = absolutePath + File.separator + "EntityP";
        String controllerTempPath = absolutePath + File.separator + "ControllerP";
        String serviceTempPath = absolutePath + File.separator + "ServiceP";
        String serviceImplTempPath = absolutePath + File.separator + "ServiceImplP";
        String mapperTempPath = absolutePath + File.separator + "MapperP";
        String mapperXmlTempPath = absolutePath + File.separator + "MapperXmlP";
        // 构建模板配置类
        return new TemplateConfig
                .Builder()
                .entity(entityTempPath)
                .controller(controllerTempPath)
                .service(serviceTempPath)
                .serviceImpl(serviceImplTempPath)
                .mapper(mapperTempPath)
                .mapperXml(mapperXmlTempPath)
                .build();
    }

    /**
     * 构建代码生成器类
     *
     * @return AutoGenerator
     */
    private static AutoGenerator buildGenerator() {
        // 建立数据库连接
        String url = properties.getProperty("database.url");
        String username = properties.getProperty("database.username");
        String password = properties.getProperty("database.password");
        DataSourceConfig dsc = new DataSourceConfig.Builder(url, username, password)
                // 自定义字段类型映射
                .typeConvert(new KunFieldTypeConverter())
                .build();
        // 构建代码生成器类
        return new AutoGenerator(dsc);
    }

    /**
     * 构建全局配置类
     *
     * @return GlobalConfig
     */
    private static GlobalConfig buildGlobalConfig() {
        // 项目目录
        String projectPath = properties.getProperty("projectPath");
        // 模块名称
        String moduleName = properties.getProperty("moduleName");
        // 作者名称
        String author = properties.getProperty("author");
        // 全局配置
        GlobalConfig.Builder globalConfigBuilder = new GlobalConfig
                .Builder()
                .outputDir(projectPath + "/" + moduleName + "/src/main/java")
                .author(author)
                // 禁止打开目录
                .disableOpenDir()
                // 日期类型策略
//                .dateType(DateType.ONLY_DATE)
                // 文件覆盖
                .fileOverride()
                .commentDate("yyyy-MM-dd HH:mm:ss");
        // 是否文件覆盖
        if (properties.getProperty("fileOverride").equals("true") ? true : false) {
            globalConfigBuilder.fileOverride();
        }
        // 构建全局配置类
        return globalConfigBuilder.build();
    }

    /**
     * 构建包配置类
     *
     * @return PackageConfig
     */
    private static PackageConfig buildPackageConfig() {
        String projectPath = properties.getProperty("projectPath");
        // 模块名称
        String moduleName = properties.getProperty("moduleName");
        // 基础包路径
        String packagePath = properties.getProperty("packagePath");
        // 需要生成的类
        boolean generateEntity = properties.getProperty("generate.entity").equals("true") ? true : false;
        boolean generateController = properties.getProperty("generate.controller").equals("true") ? true : false;
        boolean generateService = properties.getProperty("generate.service").equals("true") ? true : false;
        boolean generateServiceImpl = properties.getProperty("generate.serviceImpl").equals("true") ? true : false;
        boolean generateMapper = properties.getProperty("generate.mapper").equals("true") ? true : false;
        boolean generateMapperXml = properties.getProperty("generate.mapperXml").equals("true") ? true : false;
        // 构建包配置类
        PackageConfig.Builder builder = new PackageConfig
                .Builder()
                .parent(packagePath)
//                .moduleName(moduleName)
                .entity(generateEntity ? "entity.po" : null)
                .controller(generateController ? "controller" : null)
                .service(generateService ? "service" : null)
                .serviceImpl(generateServiceImpl ? "service.impl" : null)
                .mapper(generateMapper ? "mapper" : null)
                .xml(null)
                .moduleName(null);
        if (generateMapperXml) {
            builder.xml(generateMapperXml ? "mapper" : null)
                    .pathInfo(Collections.singletonMap(OutputFile.mapperXml,
                            projectPath + "/" + moduleName + "/src/main/resources/mapper/"));
        }
        return builder.build();
    }

    /**
     * 构建策略配置类
     *
     * @return StrategyConfig
     */
    private static StrategyConfig buildStrategyConfig() {
        // 需要生成的表
        String tables = properties.getProperty("tables");
        // 去除首尾空格
        String[] split = tables.split(",");
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i];
        }
        // 构建策略配置类
        return new StrategyConfig
                .Builder()
                // 添加需要生成的表
                .addInclude(split)
                // 设置过滤表前缀
                .addTablePrefix("t_", "k_")
                .entityBuilder()
                .naming(NamingStrategy.underline_to_camel)
//                .enableLombok()
//                .controllerBuilder()
//                .enableRestStyle()
                .build();
    }

    /**
     * 获取当前项目本地磁盘目录
     */
    private static void getPath() {
        System.out.println("当前项目本地磁盘目录->" + System.getProperty("user.dir"));
    }

}
