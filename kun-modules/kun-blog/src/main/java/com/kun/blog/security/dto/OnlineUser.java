package com.kun.blog.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 在线用户
 *
 * @author gzc
 * @since 2022/10/19 4:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUser {

    /**
     * 账号名
     */
    private String userName;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 浏览器
     */
    private String browser;
    /**
     * ip
     */
    private String ip;
    /**
     * 所在地址
     */
    private String address;
    /**
     * token
     */
    private String key;
    /**
     * 登录时间
     */
    private Date loginTime;
}
