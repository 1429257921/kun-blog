package com.kun.blog.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 坤坤云用户图片表
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
@TableName("k_user_image")
public class UserImage implements Serializable {

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
	 * 图片类型（1、头像，2、背景图）
	 */
	private Integer type;
	/**
	 * 图片名称
	 */
	private String imageName;
	/**
	 * 图片路径
	 */
	private String imagePath;
	/**
	 * 排序
	 */
	private Integer sort;
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
}