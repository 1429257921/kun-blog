package com.kun.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * 聊天记录类型表
 *
 * @author gzc
 * @since 2022-10-29 10:01:42
 */
@Data
@TableName("chat_messages_type")
public class ChatMessagesType {

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 类型名称
	 */
	@TableField("cmt_name")
	private String cmtName;
}