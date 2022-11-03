package com.kun.blog.entity.vo;

import com.kun.blog.entity.po.ChatMessages;
import lombok.Data;

import java.util.List;

/**
 * 获取聊天列表信息VO对象
 *
 * @author gzc
 * @since 2022/11/3 15:30
 **/
@Data
public class ChatListVO {
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像地址
     */
    private String alias;
    /**
     * 十条聊天记录
     */
    private List<ChatMessages> chatMessagesList;

}
