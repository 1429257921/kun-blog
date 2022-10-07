package com.kun.blog.config;

import cn.hutool.core.util.StrUtil;
import com.kun.blog.entity.vo.OnlineUser;
import com.kun.blog.service.OnlineUserService;
import com.kun.blog.util.TokenUtil;
import com.kun.common.web.config.GlobalHandlerFilter;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * TODO
 *
 * @author gzc
 * @since 2022/10/7 23:03
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthHandlerFilter extends GlobalHandlerFilter {

    private final TokenUtil tokenUtil;
    private final SecurityProperties properties;
    private final OnlineUserService onlineUserService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        super.doFilter(request, response, chain);

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestRri = httpServletRequest.getRequestURI();

        // 验证 token 是否存在
        OnlineUser onlineUser = null;
        String authToken = "";
        try {
            authToken = tokenUtil.getToken(httpServletRequest);
            if (authToken == null) {
                chain.doFilter(httpServletRequest, response);
                return;
            }
            onlineUser = onlineUserService.getOne(properties.getOnlineKey() + authToken);
        } catch (ExpiredJwtException | IOException | ServletException e) {
            log.error(e.getMessage());
        }

        String username = StrUtil.isNotBlank(authToken) ? tokenUtil.getUsernameFromToken(authToken) : null;
        if (onlineUser != null && username != null && SecurityContextHolder.getContext().getAuthentication() == null && tokenUtil.validateToken(authToken)) {
            UserDetails userDetails = tokenUtil.getUserDetails(authToken);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("set Authentication to security context for '{}', uri: {}", authentication.getName(), requestRri);
        } else {
            tokenUtil.removeToken(authToken);
            log.debug("no valid JWT token found, uri: {}", requestRri);
        }
        try {
            chain.doFilter(httpServletRequest, response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}
