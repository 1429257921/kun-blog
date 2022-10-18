package com.kun.blog.security;

import cn.hutool.core.util.StrUtil;
import com.kun.blog.service.JwtTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * token过滤器，用于鉴权和登录验证
 *
 * @author gzc
 * @since 2022/10/12 1:25
 */
@Slf4j
public class TokenFilter extends GenericFilterBean {

    private final JwtTokenService jwtTokenService;

    public TokenFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    /**
     * 执行过滤器方法
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        log.info("进入token过滤器");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String requestRri = httpServletRequest.getRequestURI();
        // 验证 token 是否存在
        String authToken = "";
        try {
            authToken = jwtTokenService.getToken(httpServletRequest);
            if (StrUtil.isBlank(authToken)) {
                chain.doFilter(httpServletRequest, response);
                return;
            }
        } catch (ExpiredJwtException e) {
            log.error("jwtToken过期,{}", e);
        }
        log.info("token-<{}", authToken);
        String userName = StrUtil.isNotBlank(authToken) ? jwtTokenService.getUsernameFromToken(authToken) : null;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (StrUtil.isNotBlank(userName)
                && securityContext.getAuthentication() == null
                && jwtTokenService.validateToken(authToken)) {
            // 获取用户详情
            UserDetails userDetails = jwtTokenService.getUserDetails(authToken);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            securityContext.setAuthentication(authentication);
            log.debug("set Authentication to security context for '{}', uri: {}", authentication.getName(), requestRri);
        } else {
            // 移除token
            jwtTokenService.removeToken(authToken);
            log.debug("no valid JWT token found, uri: {}", requestRri);
        }
        chain.doFilter(httpServletRequest, response);
    }
}
