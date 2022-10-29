package com.kun.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 聊天用户状态表
 *
 * @author gzc
 * @since 2022-10-29 10:01:42
 */
@Data
@TableName("chat_user_state")
public class ChatUserState {

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 状态名称
	 */
	@TableField("us_name")
	private String usName;
}