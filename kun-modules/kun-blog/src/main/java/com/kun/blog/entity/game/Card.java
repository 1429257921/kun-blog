package com.kun.blog.entity.game;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * 卡牌
 * 继承 JButton，就是在JButton基础上进行扩展改造
 */
public class Card extends JButton {
    public Card(String name) {
        //设置了大小
        setSize(59, 66);
        //设置不绘制边框
        setBorderPainted(false);
        //设置不填充内容区域，按钮是透明的！
        setContentAreaFilled(false);
        //设置按钮上的图片
        setIcon(new ImageIcon(ImageIcon.class.getResource("/images/" + name + ".png")));
        //设置禁用时候的灰色图片
        setDisabledIcon(new ImageIcon(ImageIcon.class.getResource("/images/" + name + "2.png")));
        //设置按钮 的 名称 name
        setName(name);
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * 判断当前牌是否盖上了 另外一张牌 card
     *
     * @param card 另外一张牌
     * @return true 表示覆盖了，false 没有盖上
     */
    public boolean covered(Card card) {
        int x1 = card.getX() - 59;
        int x2 = card.getX() + 59;
        int y1 = card.getY() - 66;
        int y2 = card.getY() + 66;
        return (getX() > x1 && getX() < x2) && (getY() > y1 && getY() < y2);
    }

    /**
     * 比较当前卡牌和另外一个卡牌是否”一样“ 名称一样
     *
     * @param other 另外一张牌
     */
    public boolean sameAs(Card other) {
        String name = getName();
        if (name == null) {
            return false;
        }
        return name.equals(other.getName());
    }

    /**
     * 删除全部的鼠标点击事件
     */
    public void removeActionListeners() {
        ActionListener[] actionListeners = getActionListeners();
        for (ActionListener l : actionListeners) {
            removeActionListener(l);
        }
    }
}
