package com.kun.blog;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * TODO
 *
 * @author gzc
 * @since 2022/12/3 17:12
 **/
public class Test1 {
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = now.toLocalDate();

        LocalTime localTime = now.toLocalTime();
        System.out.println(now);
        System.out.println(localTime);
        System.out.println(localDate);
        LocalDateTime of = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        System.out.println("of" + of);
        String format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        System.out.println(format);
        LocalDateTime parse = LocalDateTime.parse(format, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.println(parse);

    }
}
