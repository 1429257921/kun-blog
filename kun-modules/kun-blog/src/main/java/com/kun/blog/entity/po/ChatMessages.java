package com.kun.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * 聊天记录表
 *
 * @author gzc
 * @since 2022-10-29 10:01:42
 */
@Data
@TableName("chat_messages")
public class ChatMessages {

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 聊天内容
	 */
	@TableField("cm_content")
	private String cmContent;
	/**
	 * 接收状态 0成功 1失败
	 */
	private Integer status;
	/**
	 * 发送时间
	 */
	@TableField("create_time")
	private Date createTime;
	/**
	 * 0已读 1未读
	 */
	private Integer markRead;
	/**
	 * 发送者用户ID
	 */
	@TableField("from_uid")
	private Integer fromUid;
	/**
	 * 接收者用户ID
	 */
	@TableField("to_uid")
	private Integer toUid;
	/**
	 * chat_messages_type表主键ID
	 */
	@TableField("type_id")
	private Integer typeId;
}