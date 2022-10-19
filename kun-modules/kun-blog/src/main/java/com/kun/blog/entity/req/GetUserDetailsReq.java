package com.kun.blog.entity.req;

import com.kun.common.database.anno.Query;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 获取用户个人信息接口请求参数
 *
 * @author gzc
 * @since 2022/10/19 4:36
 **/
@Data
public class GetUserDetailsReq {
    /**
     * 账号名
     */
    @NotBlank(message = "账号名为空")
    @Query(propName = "user_name")
    private String userName;
    /**
     * 密码
     */
    @NotBlank(message = "密码为空")
    @Query(propName = "pass_word")
    private String passWord;
}
