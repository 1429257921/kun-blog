package com.kun.blog.service;

import cn.hutool.core.util.StrUtil;
import com.kun.blog.config.SecurityProperties;
import com.kun.blog.entity.vo.JwtUser;
import com.kun.blog.entity.vo.OnlineUser;
import com.kun.blog.util.EncryptUtils;
import com.kun.blog.util.PageUtil;
import com.kun.common.core.utils.ip.IPUtil;
import com.kun.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class OnlineUserService {

    private final SecurityProperties properties;
    private final RedisService redisService;

    /**
     * 保存在线用户信息
     *
     * @param jwtUser /
     * @param token   /
     * @param request /
     */
    public void save(JwtUser jwtUser, String token, HttpServletRequest request) {
        String job = jwtUser.getDept() + "/" + jwtUser.getJob();
        String ip = IPUtil.getIp(request);
        String browser = IPUtil.getBrowser(request);
        String address = IPUtil.getCityInfo(ip);
        OnlineUser onlineUser = null;
        try {
            onlineUser = new OnlineUser(jwtUser.getUsername(), jwtUser.getNickName(), job, browser, ip, address, EncryptUtils.desEncrypt(token), new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        redisService.setCacheObject(properties.getOnlineKey() + token, onlineUser, properties.getTokenValidityInSeconds(), TimeUnit.MILLISECONDS);

    }

    /**
     * 查询全部数据
     *
     * @param filter   /
     * @param pageable /
     * @return /
     */
    public Map<String, Object> getAll(String filter, int type, Pageable pageable) {
        List<OnlineUser> onlineUsers = getAll(filter, type);
        return PageUtil.toPage(
                PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), onlineUsers),
                onlineUsers.size()
        );
    }

    /**
     * 查询全部数据，不分页
     *
     * @param filter /
     * @return /
     */
    public List<OnlineUser> getAll(String filter, int type) {
        List<String> keys = null;
        if (type == 1) {
//            Collection<String> keys1 = redisService.keys("m-online-token*");
            keys = new ArrayList<>(redisService.keys("m-online-token*"));
        } else {
            keys = new ArrayList<>(redisService.keys(properties.getOnlineKey() + "*"));
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
     * 踢出用户
     *
     * @param key /
     * @throws Exception /
     */
    public void kickOut(String key) throws Exception {
        key = properties.getOnlineKey() + EncryptUtils.desDecrypt(key);
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
        String key = properties.getOnlineKey() + token;
        redisService.deleteObject(key);
    }

    /**
     * 导出
     *
     * @param all      /
     * @param response /
     * @throws IOException /
     */
//    public void download(List<OnlineUser> all, HttpServletResponse response) throws IOException {
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (OnlineUser user : all) {
//            Map<String, Object> map = new LinkedHashMap<>();
//            map.put("用户名", user.getUserName());
//            map.put("用户昵称", user.getNickName());
//            map.put("登录IP", user.getIp());
//            map.put("登录地点", user.getAddress());
//            map.put("浏览器", user.getBrowser());
//            map.put("登录日期", user.getLoginTime());
//            list.add(map);
//        }
//        FileUtil.downloadExcel(list, response);
//    }

    /**
     * 查询用户
     *
     * @param key /
     * @return /
     */
    public OnlineUser getOne(String key) {
        return (OnlineUser) redisService.getCacheObject(key);
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
