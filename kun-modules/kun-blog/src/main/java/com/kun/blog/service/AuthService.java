package com.kun.blog.service;

import com.kun.blog.entity.req.UserLoginReq;
import com.kun.blog.entity.req.ValidatedCodeReq;
import com.kun.blog.entity.vo.GetVerificationCodeVO;
import com.kun.blog.entity.vo.UserLoginVO;

/**
 * 秘钥接口
 *
 * @author gzc
 * @since 2022/10/7 18:18
 **/
public interface AuthService {

    /**
     * 获取验证码
     *
     * @return
     */
    GetVerificationCodeVO getCode();

    /**
     * 登录
     *
     * @param userLoginReq
     * @return
     */
    UserLoginVO login(UserLoginReq userLoginReq);

    /**
     * 校验验证码
     *
     * @param validatedCodeReq
     */
    void validatedCode(ValidatedCodeReq validatedCodeReq);
}
