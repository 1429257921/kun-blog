package com.kun.blog;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;


/**
 * 启动类
 *
 * @author gzc
 * @since 2022/9/30 21:57
 */
@Slf4j
@SpringBootApplication
@MapperScan(basePackages = "com.kun.blog.mapper.**")
public class KunBlogApplication {

    public static void main(String[] args) {
        // 设置参数 java.awt.headless=false, 也可以添加启动参数 -Djava.awt.headless=false来代替
        SpringApplicationBuilder builder = new SpringApplicationBuilder(KunBlogApplication.class);
        ConfigurableApplicationContext run = builder.headless(false).run(args);
//        printConfigInfo(SpringApplication.run(KunBlogApplication.class, args));
        printConfigInfo(run);
    }

    /**
     * 日志打印参数
     */
    private static void printConfigInfo(ConfigurableApplicationContext applicationContext) {
        // 获取当前部署的环境
        String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
        log.info("部署环境->{}", StringUtils.arrayToCommaDelimitedString(activeProfiles));
        System.out.println("(♥◠‿◠)ﾉﾞ  坤坤博客系统启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " _                      _     _             \n" +
                "| |                    | |   | |            \n" +
                "| | ___   _ _ __ ______| |__ | | ___   __ _ \n" +
                "| |/ / | | | '_ \\______| '_ \\| |/ _ \\ / _` |\n" +
                "|   <| |_| | | | |     | |_) | | (_) | (_| |\n" +
                "|_|\\_\\\\__,_|_| |_|     |_.__/|_|\\___/ \\__, |\n" +
                "                                       __/ |\n" +
                "                                      |___/ \n" +
                "\n"
        );
    }

}
