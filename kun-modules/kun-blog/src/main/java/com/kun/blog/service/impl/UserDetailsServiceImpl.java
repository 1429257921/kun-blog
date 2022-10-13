package com.kun.blog.service.impl;

import com.kun.blog.entity.dto.UserDto;
import com.kun.blog.security.dto.JwtUser;
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
@Service("userDetailsService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) {
//        UserDto user = userService.findByName(username);
//        if (user == null) {
//            throw new BadRequestException("账号不存在");
//        } else {
//            if (!user.getEnabled()) {
//                throw new BadRequestException("账号未激活");
//            }
//            return createJwtUser(user);
//        }
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword("aaa");
        return createJwtUser(userDto);

    }


    private UserDetails createJwtUser(UserDto user) {
        return new JwtUser(
                1L,
                user.getUsername(),
                "",
                "",
                user.getPassword(),
                "",
                "",
                "",
                "",
                "",
                null,
                true,
                null,
                null
        );
    }
}
