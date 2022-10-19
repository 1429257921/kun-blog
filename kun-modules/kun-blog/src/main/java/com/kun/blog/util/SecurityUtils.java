package com.kun.blog.util;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import com.kun.common.core.exception.BizException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 获取当前登录的用户
 *
 * @author gzc
 * @since 2022/10/19 3:55
 */
public class SecurityUtils {

    public static UserDetails getUserDetails() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BizException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "当前登录状态过期");
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserDetailsService userDetailsService = SpringUtil.getBean(UserDetailsService.class);
            return userDetailsService.loadUserByUsername(userDetails.getUsername());
        }
        throw new BizException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "找不到当前登录的信息");
    }

    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public static String getUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BizException(String.valueOf(HttpStatus.UNAUTHORIZED.value()), "当前登录状态过期");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    /**
     * 获取系统用户id
     *
     * @return 系统用户id
     */
    public static Long getUserId() {
        Object obj = getUserDetails();
        JSONObject json = new JSONObject(obj);
        return json.get("id", Long.class);
    }
}
