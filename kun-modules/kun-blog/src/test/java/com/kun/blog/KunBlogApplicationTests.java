package com.kun.blog;

import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KunBlogApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//        frame.setSize(490, 800);
//        frame.setLocationRelativeTo(null);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
//        System.out.println(7*1000*60*60*24);
        System.out.println("用户" + DateUtil.currentSeconds());
    }

}
