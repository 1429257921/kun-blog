package com.kun.blog.service;

import com.kun.blog.entity.req.*;
import com.kun.blog.entity.vo.GetVerificationCodeVO;
import com.kun.blog.entity.vo.SendPhoneCodeVO;
import com.kun.blog.entity.vo.UserLoginVO;
import com.kun.blog.entity.vo.UserRegisterVO;

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
     * 校验验证码
     *
     * @param validatedCodeReq
     */
    void validatedCode(ValidatedCodeReq validatedCodeReq);

    /**
     * 用户注册
     *
     * @param userRegisterReq
     * @return
     */
    UserRegisterVO register(UserRegisterReq userRegisterReq);

    /**
     * 登录
     *
     * @param userLoginReq
     * @return
     */
    UserLoginVO login(UserLoginReq userLoginReq) throws Exception;

    /**
     * 发送手机验证码
     *
     * @param phone 手机号
     * @return SendPhoneCodeVO
     */
    SendPhoneCodeVO sendPhoneCode(String phone);

    /**
     * 验证手机短信验证码
     *
     * @param validatedCodeReq 请求参数
     */
    void validatedPhoneCode(ValidatedPhoneCodeReq validatedCodeReq);

}

