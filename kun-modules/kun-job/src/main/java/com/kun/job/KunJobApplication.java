package com.kun.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

/**
 * 启动类
 *
 * @author gzc
 * @since 2022/11/9 10:53
 */
@Slf4j
@SpringBootApplication
public class KunJobApplication {

    public static void main(String[] args) {
        printConfigInfo(SpringApplication.run(KunJobApplication.class, args));
    }

    /**
     * 日志打印参数
     */
    private static void printConfigInfo(ConfigurableApplicationContext applicationContext) {
        // 获取当前部署的环境
        String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
        log.info("部署环境->{}", StringUtils.arrayToCommaDelimitedString(activeProfiles));
        System.out.println("(♥◠‿◠)ﾉﾞ  坤坤云定时任务系统启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
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
