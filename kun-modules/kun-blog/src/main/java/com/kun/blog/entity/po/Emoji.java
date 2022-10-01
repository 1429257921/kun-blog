package com.kun.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 坤坤表情包
 *
 * @author gzc
 * @since 2022-10-01 18:49:43
 */
@Data
@TableName("emoji")
public class Emoji {

	/**
	 * 主键id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 表情包类型ID(emoji_type表主键id)
	 */
	@TableField("type_id")
	private Integer typeId;
	/**
	 * 表情包远程网址
	 */
	private String url;
	/**
	 * 表情包保存地址
	 */
	private String path;
	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private LocalDateTime createTime;
	/**
	 * 更新时间
	 */
	@TableField("update_time")
	private LocalDateTime updateTime;
	/**
	 * 表情包名称
	 */
	private String name;
	/**
	 * 备注
	 */
	private String remark;
}