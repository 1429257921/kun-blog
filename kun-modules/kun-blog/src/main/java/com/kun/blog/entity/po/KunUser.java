package com.kun.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户表
 *
 * @author gzc
 * @since 2022-10-07 21:05:29
 */
@Data
@TableName("kun_user")
public class KunUser {

    /**
     * 用户id
     */
    @TableId(value = "uid", type = IdType.AUTO)
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
    @TableField("real_name")
    private String realName;
    /**
     * 生日
     */
    private Integer birthday;
    /**
     * 身份证号码
     */
    @TableField("card_id")
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
     * 手机号码
     */
    private String phone;
    /**
     * 添加ip
     */
    @TableField("add_ip")
    private String addIp;
    /**
     * 添加时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 最后一次登录时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 最后一次登录ip
     */
    @TableField("last_ip")
    private String lastIp;
    /**
     * 用户余额
     */
    @TableField("now_money")
    private BigDecimal nowMoney;
    /**
     * 佣金金额
     */
    @TableField("brokerage_price")
    private BigDecimal brokeragePrice;
    /**
     * 用户剩余积分
     */
    private BigDecimal integral;
    /**
     * 连续签到天数
     */
    @TableField("sign_num")
    private Integer signNum;
    /**
     * 1为正常，0为禁止
     */
    private Boolean status;
    /**
     * 等级
     */
    private Integer level;
    /**
     * 推广元id
     */
    @TableField("spread_uid")
    private Long spreadUid;
    /**
     * 推广员关联时间
     */
    @TableField("spread_time")
    private Date spreadTime;
    /**
     * 用户类型
     */
    @TableField("user_type")
    private String userType;
    /**
     * 是否为推广员
     */
    @TableField("is_promoter")
    private Integer isPromoter;
    /**
     * 用户购买次数
     */
    @TableField("pay_count")
    private Integer payCount;
    /**
     * 下级人数
     */
    @TableField("spread_count")
    private Integer spreadCount;
    /**
     * 详细地址
     */
    private String addres;
    /**
     * 管理员编号
     */
    private Integer adminid;
    /**
     * 用户登陆类型，h5,wechat,routine
     */
    @TableField("login_type")
    private String loginType;
    /**
     * 微信用户json信息
     */
    @TableField("wx_profile")
    private String wxProfile;
    /**
     * 是否删除
     */
    @TableField("is_del")
    private Boolean isDel;
}