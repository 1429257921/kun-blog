package com.kun.blog.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.kun.blog.enums.DeleteFlagEnum;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 坤坤云用户登录记录表
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
@TableName("k_user_login_record")
public class UserLoginRecord implements Serializable {

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
     * 用户登录授权主键ID
     */
    private Integer authId;
    /**
     * 操作类型（0、登录成功，1、登出成功，2、登录失败，3、登出失败）
     */
    private Integer loginStatus;
    /**
     * 客户端版本号
     */
    private String appVersion;
    /**
     * 设备硬件地址
     */
    private String mac;
    /**
     * 登录ip
     */
    private String ip;
    /**
     * 登录系统，IOS等
     */
    private String os;
    /**
     * 系统版本
     */
    private String osVersion;
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
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}