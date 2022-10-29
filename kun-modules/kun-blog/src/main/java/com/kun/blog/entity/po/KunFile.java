package com.kun.blog.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 文件信息
 *
 * @author gzc
 * @since 2022-10-29 10:01:42
 */
@Data
@TableName("kun_file")
public class KunFile {

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 上传日期
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 删除的URL
     */
    @TableField("delete_url")
    private String deleteUrl;
    /**
     * 文件名称
     */
    @TableField("file_name")
    private String fileName;
    /**
     * 图片高度
     */
    private String height;
    /**
     * 文件大小
     */
    private String size;
    /**
     * 文件地址
     */
    private String url;
    /**
     * 用户名称
     */
    @TableField("user_name")
    private String userName;
    /**
     * 图片宽度
     */
    private String width;
    /**
     * 文件的MD5值
     */
    private String md5code;
    /**
     * 文件类型 0图片
     */
    @TableField("file_type")
    private Integer fileType;
}