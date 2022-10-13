package com.kun.blog.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author hupeng
 * @date 2020-05-14
 */
@Data
public class UserDto implements Serializable {


    private Long id;

    private String username;

    private String nickName;

    private String sex;

    private String avatar;

    private String email;

    private String phone;

    private Boolean enabled;

    @JsonIgnore
    private String password;

    private Timestamp lastPasswordResetTime;

    private Long deptId;

    private Timestamp createTime;
}
