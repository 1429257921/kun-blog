package com.kun.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.*;

@SpringBootTest
class KunBlogApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(490, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
