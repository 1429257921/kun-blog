package com.kun.blog.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户表
 *
 * @author gzc
 * @since 2022-10-29 10:01:42
 */
@Data
@TableName("kun_user")
public class KunUser {

    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Long uid;
    /**
     * 用户账户(跟accout一样)
     */
    private String username;
    /**
     * 用户密码（跟pwd）
     */
    private String password;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 生日
     */
    private Integer birthday;
    /**
     * 身份证号码
     */
    private String cardId;
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
     * 年龄
     */
    private Integer age;
    /**
     * 性别 0女 1男
     */
    private String sex;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 详细地址
     */
    private String addres;
    /**
     * 连续签到天数
     */
    private Integer signNum;
    /**
     * 等级
     */
    private Integer level;
    /**
     * 用户登陆类型，h5,wechat,routine
     */
    private String loginType;
    /**
     * 用户余额
     */
    private BigDecimal nowMoney;
    /**
     * 用户剩余积分
     */
    private BigDecimal integral;
    /**
     * 创建时的ip
     */
    private String addIp;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 最后一次登录ip
     */
    @TableField("last_ip")
    private String lastIp;
    /**
     * 最后一次登录时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 1为正常，0为禁止
     */
    private Boolean status;
    /**
     * 删除标识 （0、正常，1、删除）
     */
    @TableField("is_del")
    @TableLogic
    private Byte delete = 0;
}