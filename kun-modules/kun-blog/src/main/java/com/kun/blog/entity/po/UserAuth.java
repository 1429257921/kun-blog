package com.kun.blog.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.kun.blog.enums.UserAuthLoginTypeEnum;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 坤坤云用户登录授权表
 * </p>
 *
 * @author gzc
 * @since 2023-01-05 18:04:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@TableName("k_user_auth")
public class UserAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户主键ID
     */
    private Integer userId;
    /**
     * 登录类型（1、手机号验证码登录 ，2、手机号一键登录， 3、账号密码登录）
     * {@link UserAuthLoginTypeEnum}
     */
    private Integer loginType;
    /**
     * 手机号、用户名或第三方应用的唯一标识
     */
    private String account;
    /**
     * 密码(站内的保存密码，站外的不保存或保存token)
     */
    private String password;
    /**
     * 备注
     */
    private String remark;
    /**
     * 删除标志（0、正常，1、删除）
     */
    @TableField("del_flag")
    @TableLogic
    private Byte deleted = 0;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, updateStrategy = FieldStrategy.NOT_EMPTY)
    private LocalDateTime updateTime;
}