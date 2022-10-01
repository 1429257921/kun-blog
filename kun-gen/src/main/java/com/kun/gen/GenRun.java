package com.kun.gen;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * 代码生成
 * 注意： 请先修改resources目录下的generator.properties文件中的配置，无误则执行当前类下的main方法
 *
 * @author gzc
 * @since 2022/10/1 11:09
 **/
public class GenRun {

    /**
     * 代码生成入口
     * 注意： 请先修改resources目录下的generator.properties文件中的配置
     */
    public static void main(String[] args) {
        doGenerator();
        getPath();
    }

    private static Properties properties = new Properties();

    static {
        // 读取resources目录下的配置文件
        InputStream inputStream = GenRun.class.getClassLoader().getResourceAsStream("generator.properties");
        try {
            properties.load(IoUtil.getReader(inputStream, Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 代码生成实现
     * 提示：如果不需要生成controller相关代码，设置packageConfig中的controller为""，设置templateConfig中的controller为null
     */
    private static void doGenerator() {
        // 建立数据库连接
        String url = properties.getProperty("database.url");
        String username = properties.getProperty("database.username");
        String password = properties.getProperty("database.password");
        DataSourceConfig dsc = new DataSourceConfig.Builder(url, username, password).build();

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator(dsc);
        // 模块名称
        String moduleName = properties.getProperty("moduleName");
        // 项目目录
        String projectPath = properties.getProperty("projectPath");
        // 作者名称
        String author = properties.getProperty("author");
        // 基础包路径
        String packagePath = properties.getProperty("packagePath");
        // 需要生成的表
        String tables = properties.getProperty("tables");
        // 代码生成后是否打开磁盘目录
        String openDir = properties.getProperty("openDir");

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig
                .Builder()
                .outputDir(projectPath + "/" + moduleName + "/src/main/java")
                .author(author)
//                .openDir("true".equals(openDir))
                .commentDate("yyyy-MM-dd HH:mm:ss")
                .build();

        // 包配置
        PackageConfig packageConfig = new PackageConfig
                .Builder()
                .parent(packagePath)
//                .moduleName(moduleName)
                .controller("")
                .entity("entity.po")
                .mapper("mapper")
                .service("service")
                .serviceImpl("service.impl")
                .moduleName(null)
                .build();

        // 配置模板
        String absolutePath = File.separator + "templates";
        String mapperTempPath = absolutePath + File.separator + "MapperP";
        String entityTempPath = absolutePath + File.separator + "EntityP";
        String serviceTempPath = absolutePath + File.separator + "ServiceP";
        String serviceImplTempPath = absolutePath + File.separator + "ServiceImplP";
        TemplateConfig templateConfig = new TemplateConfig
                .Builder()
                .mapper(mapperTempPath)
                .service(serviceTempPath)
                .serviceImpl(serviceImplTempPath)
                .entity(entityTempPath)
                .mapperXml(null)
                .controller(null)
                .build();

        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig
                .Builder()
                .addInclude(tables.split(","))
                .entityBuilder()
                .naming(NamingStrategy.underline_to_camel)
//                .enableLombok()
//                .controllerBuilder()
//                .enableRestStyle()
                .build();

        mpg.global(globalConfig);
        mpg.packageInfo(packageConfig);
        mpg.template(templateConfig);
        mpg.strategy(strategyConfig);
        // 开始生成代码文件
        mpg.execute(new FreemarkerTemplateEngine());
    }


    /**
     * 获取当前项目本地磁盘目录
     */
    private static void getPath() {
        System.out.println("当前项目本地磁盘目录->" + System.getProperty("user.dir"));
    }

}
