package com.kun.blog.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.kun.blog.enums.DeleteFlagEnum;
import com.kun.blog.enums.UserAuthLoginTypeEnum;
import com.kun.blog.enums.UserSexEnum;
import com.kun.blog.enums.UserStatusEnum;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 坤坤云用户表
 * </p>
 *
 * @author gzc
 * @since 2023-01-05 17:59:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@TableName("k_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户表主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 头像
     */
    private String headPortrait;
    /**
     * 昵名
     */
    private String nickName;
    /**
     * 性别（0、男，1、女）
     * {@link UserSexEnum}
     */
    private Integer sex;
    /**
     * 生日
     */
    private LocalDate birthday;
    /**
     * 个性签名
     */
    private String personalSignature;
    /**
     * 行业
     */
    private String industry;
    /**
     * 地区
     */
    private String area;
    /**
     * 注册来源（1、手机号验证码登录 ，2、手机号一键登录， 3、账号密码登录）
     * {@link UserAuthLoginTypeEnum}
     */
    private Integer registerSource;
    /**
     * 账号状态（0、禁用，1、启用，3、注销）
     * {@link UserStatusEnum}
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 删除标志（0、正常，1、删除）
     * {@link DeleteFlagEnum}
     */
    @TableField("del_flag")
    @TableLogic
    private Byte deleted = 0;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, updateStrategy = FieldStrategy.NOT_EMPTY)
    private LocalDateTime updateTime;
}