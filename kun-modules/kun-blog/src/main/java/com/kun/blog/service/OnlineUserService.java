package com.kun.blog.service;

import cn.hutool.core.util.StrUtil;
import com.kun.blog.security.JwtProperties;
import com.kun.blog.security.dto.JwtUser;
import com.kun.blog.security.dto.OnlineUser;
import com.kun.blog.util.EncryptUtils;
import com.kun.common.core.utils.ip.IPUtil;
import com.kun.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 在线用户操作
 *
 * @author gzc
 * @since 2022/10/19 4:23
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class OnlineUserService {

    private final RedisService redisService;
    private final JwtProperties jwtProperties;

    /**
     * 保存在线用户信息
     */
    public void save(JwtUser jwtUser, String token, HttpServletRequest request) {
        String ip = IPUtil.getIp(request);
        String browser = IPUtil.getBrowser(request);
        String address = IPUtil.getCityInfo(ip);
        OnlineUser onlineUser = null;
        try {
            onlineUser = new OnlineUser(jwtUser.getUsername(), jwtUser.getNickName(), browser, ip, address, EncryptUtils.desEncrypt(token), new Date());
        } catch (Exception e) {
            log.error("对称加密发生异常->{}", e);
        }
        redisService.setCacheObject(jwtProperties.getOnlineKey() + token, onlineUser,
                jwtProperties.getTokenValidityInSeconds(), TimeUnit.MILLISECONDS);
    }

    /**
     * 踢出用户
     *
     * @param key /
     * @throws Exception /
     */
    public void kickOut(String key) throws Exception {
        key = jwtProperties.getOnlineKey() + EncryptUtils.desDecrypt(key);
        redisService.deleteObject(key);

    }

    /**
     * 踢出移动端用户
     *
     * @param key /
     * @throws Exception /
     */
    public void kickOutT(String key) throws Exception {
        String keyt = "m-online-token" + EncryptUtils.desDecrypt(key);
        redisService.deleteObject(keyt);

    }

    /**
     * 退出登录
     *
     * @param token /
     */
    public void logout(String token) {
        String key = jwtProperties.getOnlineKey() + token;
        redisService.deleteObject(key);
    }

    /**
     * 查询用户
     *
     * @param key /
     * @return /
     */
    public OnlineUser getOne(String key) {
        return redisService.getCacheObject(key);
    }

    /**
     * 查询全部数据，不分页
     *
     * @param filter /
     * @return /
     */
    public List<OnlineUser> getAll(String filter, int type) {
        List<String> keys;
        if (type == 1) {
            keys = new ArrayList<>(redisService.keys("m-online-token*"));
        } else {
            keys = new ArrayList<>(redisService.keys(jwtProperties.getOnlineKey() + "*"));
        }

        Collections.reverse(keys);
        List<OnlineUser> onlineUsers = new ArrayList<>();
        for (String key : keys) {
            OnlineUser onlineUser = redisService.getCacheObject(key);
            if (StrUtil.isNotBlank(filter)) {
                if (onlineUser.toString().contains(filter)) {
                    onlineUsers.add(onlineUser);
                }
            } else {
                onlineUsers.add(onlineUser);
            }
        }
        onlineUsers.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUsers;
    }

    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     *
     * @param userName 用户名
     */
    public void checkLoginOnUser(String userName, String igoreToken) {
        List<OnlineUser> onlineUsers = getAll(userName, 0);
        if (onlineUsers == null || onlineUsers.isEmpty()) {
            return;
        }
        for (OnlineUser onlineUser : onlineUsers) {
            if (onlineUser.getUserName().equals(userName)) {
                try {
                    String token = EncryptUtils.desDecrypt(onlineUser.getKey());
                    if (StrUtil.isNotBlank(igoreToken) && !igoreToken.equals(token)) {
                        this.kickOut(onlineUser.getKey());
                    } else if (StrUtil.isBlank(igoreToken)) {
                        this.kickOut(onlineUser.getKey());
                    }
                } catch (Exception e) {
                    log.error("checkUser is error", e);
                }
            }
        }
    }

}
