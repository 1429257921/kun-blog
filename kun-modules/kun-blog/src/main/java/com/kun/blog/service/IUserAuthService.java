package com.kun.blog.service;

import com.kun.blog.entity.po.UserAuth;
import com.kun.blog.enums.UserAuthLoginTypeEnum;
import com.kun.common.database.service.BaseService;

import java.util.List;

/**
 * 坤坤云用户登录授权表业务接口
 *
 * @author gzc
 * @since 2023-01-05 18:04:37
 */
public interface IUserAuthService extends BaseService<UserAuth> {
    /**
     * 获取登录授权信息
     *
     * @param userId        用户ID
     * @param loginTypeEnum 用户登录类型枚举
     * @return UserAuth
     */
    UserAuth getByUserId(Integer userId, UserAuthLoginTypeEnum loginTypeEnum);

    /**
     * 获取登录授权信息集合
     *
     * @param userId 用户ID
     * @return List<UserAuth>
     */
    List<UserAuth> getByUserId(Integer userId);

    /**
     * 获取登录授权信息集合
     *
     * @param userName      手机号
     * @param loginTypeEnum 用户登录类型枚举
     * @return UserAuth
     */
    UserAuth getByPhone(String userName, UserAuthLoginTypeEnum loginTypeEnum);
}
