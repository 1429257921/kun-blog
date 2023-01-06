package com.kun.common.database.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.github.pagehelper.PageHelper;
import com.kun.common.database.interceptor.SqlLogInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Properties;

/**
 * MybatisPlus配置
 *
 * @author gzc
 * @since 2022/9/30 21:27
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        return new MybatisPlusInterceptor();
    }

    /**
     * sql 日志
     */
//    @Bean
//    @Profile({"local", "dev", "test"})
//    @ConditionalOnProperty(value = "mybatis-plus.sql-log.enable", matchIfMissing = true)
//    public SqlLogInterceptor sqlLogInterceptor() {
//        return new SqlLogInterceptor();
//    }

    /**
     * 配置mybatis的分页插件pageHelper
     * @return
     */
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        //配置mysql数据库的方言
        properties.setProperty("dialect", "mysql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}
