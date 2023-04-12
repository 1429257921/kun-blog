package com.kun.blog.entity.dto;

import com.kun.blog.enums.SensitiveWordModeEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 敏感词DTO
 *
 * @author gzc
 * @since 2023/2/13 12:11
 **/
@Data
public class SensitiveWordDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 敏感词
     */
    private String word;
    /**
     * 影响方式（1、屏蔽，2、脱敏，3、警告）
     */
    private SensitiveWordModeEnum mode;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
