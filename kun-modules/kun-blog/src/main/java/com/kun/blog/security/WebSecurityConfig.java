package com.kun.blog.security;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.kun.blog.anno.AnonymousAccess;
import com.kun.blog.service.JwtTokenService;
import com.kun.common.core.constants.ThreadLocalMapConstants;
import com.kun.common.core.utils.ThreadLocalUtil;
import com.kun.common.log.anno.APIMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Spring Security配置类
 *
 * @author gzc
 * @since 2022/10/12 1:03
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     * 配置放行url
     *
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        // 获取放行的url
        Set<String> anonymousUrls = getAnonymousUrls();
        web.ignoring()
                // 放行OPTIONS请求
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers(anonymousUrls.toArray(new String[0]));
    }

    /**
     * 配置密码加密方式
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 密码加密方式
        auth.userDetailsService(userDetailsService).passwordEncoder(new DefaultPasswordEncoder());
    }

    /**
     * 配置过滤器
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 关闭cors
                .csrf().disable()
                .exceptionHandling()
                //配置未授权处理
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler())
                // 防止iframe 造成跨域
                .and()
                .headers()
                .frameOptions()
                .disable()
                // 不创建会话
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 授权请求
                .and().authorizeRequests()
                // 所有请求都需要认证
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new TokenFilter(jwtTokenService), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 搜寻匿名标记 url： @AnonymousAccess
     */
    private Set<String> getAnonymousUrls() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap =
                requestMappingHandlerMapping.getHandlerMethods();
        Set<String> anonymousUrls = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
            if (anonymousAccess != null) {
                Set<PathPattern> patterns = infoEntry.getKey().getPathPatternsCondition().getPatterns();
                if (CollUtil.isNotEmpty(patterns)) {
                    for (PathPattern pattern : patterns) {
                        if (pattern != null) {
                            anonymousUrls.add(pattern.getPatternString());
                        }
                    }
                }
            }
        }
        return anonymousUrls;
    }
}
