package com.kun.blog.service;

import com.kun.blog.entity.po.User;
import com.kun.blog.entity.req.SetupPassWordReq;
import com.kun.common.database.service.BaseService;

/**
 * 坤坤云用户表业务接口
 *
 * @author gzc
 * @since 2023-01-05 17:59:28
 */
public interface IUserService extends BaseService<User> {

    /**
     * 获取用户信息
     *
     * @param phone 手机号
     * @return User
     */
    User getByPhone(String phone);

    /**
     * 设置密码
     *
     * @param setupPassWordReq 请求参数
     */
    void setupPassword(SetupPassWordReq setupPassWordReq);
}
