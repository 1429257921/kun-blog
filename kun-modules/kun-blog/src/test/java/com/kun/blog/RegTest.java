package com.kun.blog;

import cn.hutool.core.lang.RegexPool;
import cn.hutool.core.util.ReUtil;

import java.util.Scanner;

/**
 * TODO
 *
 * @author gzc
 * @since 2022/10/16 2:19
 **/
public class RegTest {
    //    static String userNameReg = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z0-9-\\\\_]{6,16}$";
    static String userNameReg1 = "^[a-zA-Z0-9-_]{6,20}$";
    static String userPassWord = "^[a-zA-Z0-9]{6,18}$";

    public static void main(String[] args) {
//        while (true) {
//            String nextLine = new Scanner(System.in).nextLine();
        System.out.println(ReUtil.isMatch(userNameReg1, "abcA123"));
        System.out.println(ReUtil.isMatch(userPassWord, "abc23"));
        System.out.println(ReUtil.isMatch("^1[3578]\\d{9}$", "03002094938"));
//        }

    }
}
