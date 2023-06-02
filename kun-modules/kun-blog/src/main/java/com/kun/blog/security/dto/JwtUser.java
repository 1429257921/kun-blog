package com.kun.blog.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.kun.blog.enums.UserSexEnum;
import com.kun.blog.enums.UserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * jwt用户详情
 *
 * @author gzc
 * @since 2023/1/5 20:47
 */
@Getter
@AllArgsConstructor
public class JwtUser implements UserDetails {
    /**
     * 用户主键ID
     */
    private final Integer id;
    /**
     * 手机号
     */
    private final String phone;
    /**
     * 昵称
     */
    private final String nickName;
    /**
     * 性别（0、男，1、女）
     * {@link UserSexEnum}
     */
    private final Integer sex;
    /**
     * 性别描述
     */
    private final String sexDesc;
    /**
     * 密码
     */
    @JsonIgnore
    private final String password;
    /**
     * 头像
     */
    private final String headPortrait;
    /**
     * 创建时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;
    /**
     * 账号状态（0、禁用，1、启用，3、注销）
     * {@link UserStatusEnum}
     */
    private Integer status;

    @JsonIgnore
    private final Collection<SimpleGrantedAuthority> authorities;

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.phone;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /**
     * 获取当前用户的角色集合
     *
     * @return 角色集合
     */
    public Collection getRoles() {
        if (authorities != null) {
            return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        }
        return null;
    }

    /**
     * 账号启用状态
     *
     * @return 是否启用
     */
    @Override
    public boolean isEnabled() {
        return UserStatusEnum.ACTIVE.getCode() == this.status;
    }
}
