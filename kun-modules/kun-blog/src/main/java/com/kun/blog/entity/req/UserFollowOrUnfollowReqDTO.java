package com.kun.blog.entity.req;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 社交-用户关注/取消关注接口请求参数
 *
 * @author gzc
 * @since 2022/12/29 14:46
 **/
@Data
@ToString
public class UserFollowOrUnfollowReqDTO implements Serializable {
    /**
     * 关注者用户ID
     */
    @NotNull(message = "关注者用户ID为空")
    private Long userId;
    /**
     * 被关注者用户ID
     */
    @NotNull(message = "被关注者用户ID为空")
    private Long followUserId;
    /**
     * 操作类型(0、关注，1、取消关注)
     */
    @NotNull(message = "操作类型为空")
    private Integer type;
}
