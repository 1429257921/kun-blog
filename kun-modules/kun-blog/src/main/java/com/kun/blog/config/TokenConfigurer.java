package com.kun.blog.config;

import com.kun.blog.service.OnlineUserService;
import com.kun.blog.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * token配置
 *
 * @author gzc
 * @since 2022/10/7 22:45
 */
@RequiredArgsConstructor
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenUtil tokenUtil;
    private final SecurityProperties properties;
    private final OnlineUserService onlineUserService;

    @Override
    public void configure(HttpSecurity http) {
        AuthHandlerFilter customFilter = new AuthHandlerFilter(tokenUtil, properties, onlineUserService);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
