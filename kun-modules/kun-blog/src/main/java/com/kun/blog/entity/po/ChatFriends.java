package com.kun.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 聊天好友表
 *
 * @author gzc
 * @since 2022-10-29 10:01:42
 */
@Data
@TableName("chat_friends")
public class ChatFriends {

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 自己用户主键id
	 */
	private Integer uid;
	/**
	 * 朋友用户主键id
	 */
	@TableField("cf_uid")
	private Integer cfUid;
	/**
	 * 备注
	 */
	@TableField("cf_name")
	private String cfName;
}