package com.kun.blog;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 多线程测试
 *
 * @author gzc
 * @since 2022/12/30 13:25
 **/
public class ConcurrentTest {
    public static void main(String[] args) throws IOException {
        System.out.println(convertLastVisitorTime(DateUtil.parse("2022-12-30 17:48:12").toLocalDateTime()));
        System.out.println(convertLastVisitorTime(DateUtil.parse("2022-12-30 17:25:12").toLocalDateTime()));
        System.out.println(convertLastVisitorTime(DateUtil.parse("2022-12-30 12:45:12").toLocalDateTime()));
        System.out.println(convertLastVisitorTime(DateUtil.parse("2022-12-25 17:45:12").toLocalDateTime()));
        System.out.println(convertLastVisitorTime(DateUtil.parse("2022-11-20 17:45:12").toLocalDateTime()));
        System.out.println(convertLastVisitorTime(DateUtil.parse("2022-10-20 17:45:12").toLocalDateTime()));

//        ConcurrencyTester concurrencyTester = ThreadUtil.concurrencyTest(100, ConcurrentTest::test1);
//        concurrencyTester.close();
    }

    /**
     * 转换最后访问时间展示格式
     *
     * @param lastVisitorTime 最后访问时间
     * @return 字符串
     */
    private static String convertLastVisitorTime(LocalDateTime lastVisitorTime) {
        String lastVisitorTimeStr = "";
        LocalDateTime now = LocalDateTime.now();
        // 10分钟内, 展示刚刚访问过
        if (now.isBefore(lastVisitorTime.plusMinutes(10))) {
            lastVisitorTimeStr = "刚刚访问过";
        }
        // 1小时内, 展示xx:xx
        else if (now.isBefore(lastVisitorTime.plusHours(1))) {
            lastVisitorTimeStr = lastVisitorTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        // 1-24小时内, 展示x小时前访问
        else if (now.isBefore(lastVisitorTime.plusHours(24))) {
            Date date1 = Date.from(lastVisitorTime.toInstant(ZoneOffset.of("+8")));
            Date date2 = Date.from(now.toInstant(ZoneOffset.of("+8")));
            long between = DateUtil.between(date1, date2, DateUnit.HOUR);
            lastVisitorTimeStr = between + "小时前访问过";
        }
        // 1-30天, 展示x天前访问
        else if (now.isBefore(lastVisitorTime.plusDays(30))) {
            Date date1 = Date.from(lastVisitorTime.toInstant(ZoneOffset.of("+8")));
            Date date2 = Date.from(now.toInstant(ZoneOffset.of("+8")));
            long between = DateUtil.between(date1, date2, DateUnit.DAY);
            lastVisitorTimeStr = between + "天前访问过";
        }
        // 大于30天, 展示30天前访问
        else if (now.isAfter(lastVisitorTime.plusDays(30))) {
            lastVisitorTimeStr = "30天前访问过";
        }

        return lastVisitorTimeStr;
    }

    private static void test1() {
        try {
            String body = "";
            int i = RandomUtil.randomInt(0, 4);
            System.out.println(i);
            if (i == 0) {
                body = "{\"userId\":999,\"followUserId\":888,\"type\":0}";
            } else if (i == 1) {
                body = "{\"userId\":888,\"followUserId\":999,\"type\":0}";
            } else if (i == 2) {
                body = "{\"userId\":999,\"followUserId\":888,\"type\":1}";
            } else if (i == 3) {
                body = "{\"userId\":888,\"followUserId\":888,\"type\":1}";
            }

            String post = HttpUtil.post("http://127.0.0.1:9200/kun-blog/test/aaa", body);
            System.out.println(post);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
