package com.kun.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 系统日志
 *
 * @author gzc
 * @since 2022-10-07 21:05:29
 */
@Data
@TableName("kun_log")
public class KunLog {

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private Date createTime;
	/**
	 * 详情
	 */
	private String description;
	/**
	 * 错误信息
	 */
	@TableField("exception_detail")
	private String exceptionDetail;
	/**
	 * 日志类型
	 */
	@TableField("log_type")
	private String logType;
	/**
	 * 方法
	 */
	private String method;
	/**
	 * 请求参数
	 */
	private String params;
	/**
	 * 请求IP
	 */
	@TableField("request_ip")
	private String requestIp;
	/**
	 * 请求时间
	 */
	private Long time;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 浏览器
	 */
	private String browser;
	/**
	 * 类型
	 */
	private Integer type;
	/**
	 * 用户id
	 */
	private Long uid;
	/**
	 * 更新时间
	 */
	@TableField("update_time")
	private Date updateTime;
	/**
	 * 是否删除
	 */
	@TableField("is_del")
	private Boolean isDel;
}