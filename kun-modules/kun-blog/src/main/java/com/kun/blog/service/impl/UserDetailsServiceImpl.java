package com.kun.blog.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kun.blog.entity.po.KunUser;
import com.kun.blog.security.dto.JwtUser;
import com.kun.blog.service.IKunUserService;
import com.kun.common.core.exception.Assert;
import com.kun.common.core.exception.BizException;
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

    private final IKunUserService kunUserService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        KunUser kunUser = new LambdaQueryChainWrapper<>(kunUserService.getBaseMapper())
                .eq(KunUser::getUsername, username)
                .one();
        Assert.notNull(kunUser, "账号不存在");
        if (kunUser.getStatus() == null || !kunUser.getStatus()) {
            throw new BizException("账号异常或被封禁");
        }
        if (kunUser.getIsDel() == null || kunUser.getIsDel()) {
            throw new BizException("账号已注销");
        }
        return createJwtUser(kunUser);
    }


    private UserDetails createJwtUser(KunUser kunUser) {
        return new JwtUser(
                kunUser.getUid(),
                kunUser.getUsername(),
                kunUser.getNickname(),
                kunUser.getSex(),
                kunUser.getPassword(),
                kunUser.getAvatar(),
                "",
                kunUser.getPhone(),
                null,
                true,
                kunUser.getCreateTime(),
                null
        );
    }
}
