package com.kun.blog;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * TODO
 *
 * @author gzc
 * @since 2022/12/3 17:12
 **/
public class Test1 {
    public static void main(String[] args) {
        String time = "1669978847";
//        DateTime parse = DateUtil.parseDate(time);
        System.out.println(DateUtil.current());
        System.out.println(DateUtil.currentSeconds());
        System.out.println(new DateTime(time).toDateStr());
    }
}
