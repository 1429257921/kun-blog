package com.kun.blog.service.impl;

import com.kun.blog.entity.po.User;
import com.kun.blog.enums.UserSexEnum;
import com.kun.blog.security.dto.JwtUser;
import com.kun.blog.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring Security 核心用户接口的实现类
 *
 * @author gzc
 * @since 2022/10/12 1:18
 */
@Slf4j
@RequiredArgsConstructor
@Service("userDetailsService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userService.getByPhone(username);
        JwtUser jwtUser = null;
        if (user != null) {
            jwtUser = new JwtUser(
                    user.getId(),
                    user.getPhone(),
                    user.getNickName(),
                    user.getSex(),
                    UserSexEnum.getName(user.getSex()),
                    null,
                    user.getHeadPortrait(),
                    user.getCreateTime(),
                    user.getStatus(),
                    null
            );
        }
        return jwtUser;
    }


}
