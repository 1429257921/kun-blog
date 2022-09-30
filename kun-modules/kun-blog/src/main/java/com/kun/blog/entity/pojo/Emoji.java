package com.kun.blog.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 坤坤表情包(Emoji)实体类
 *
 * @author gzc
 * @since 2022-09-30 22:28:03
 */
@Data
@TableName("emoji")
public class Emoji {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 表情包类型ID
     */
    @TableField("emoji_type_id")
    private Integer emojiTypeId;

}