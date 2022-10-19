package com.kun.blog.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author gzc
 * @since 2022/10/19 7:34
 **/
@Data
public class GetUserDetailsVO {

    /**
     * 用户id
     */
    private Long uid;
    /**
     * 用户账户(跟accout一样)
     */
    private String username;
    /**
     * 生日
     */
    private Integer birthday;
    /**
     * 性别 0女 1男
     */
    private String sex;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 用户备注
     */
    private String mark;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 添加时间
     */
    private Date createTime;
    /**
     * 最后一次登录时间
     */
    private Date updateTime;
    /**
     * 最后一次登录ip
     */
    private String lastIp;
    /**
     * 详细地址
     */
    private String addres;
}
