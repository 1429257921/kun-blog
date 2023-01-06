package com.kun.blog.service.impl;

import com.kun.blog.entity.po.User;
import com.kun.blog.entity.po.UserAuth;
import com.kun.blog.enums.UserAuthLoginTypeEnum;
import com.kun.blog.mapper.UserAuthMapper;
import com.kun.blog.service.IUserAuthService;
import com.kun.blog.service.IUserService;
import com.kun.common.database.service.impl.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gzc
 * @since 2023-01-05 18:04:37
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserAuthServiceImpl extends BaseServiceImpl<UserAuthMapper, UserAuth> implements IUserAuthService {

    @Override
    public UserAuth getByUserId(Integer userId, UserAuthLoginTypeEnum loginTypeEnum) {
        return lambdaQuery()
                .eq(UserAuth::getUserId, userId)
                .eq(UserAuth::getLoginType, loginTypeEnum.getCode())
                .one();
    }

    @Override
    public List<UserAuth> getByUserId(Integer userId) {
        return lambdaQuery()
                .eq(UserAuth::getUserId, userId)
                .list();
    }

    @Override
    public UserAuth getByPhone(String userName, UserAuthLoginTypeEnum loginTypeEnum) {

        return null;
    }
}
