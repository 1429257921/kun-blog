package com.kun.blog.service;

import com.kun.blog.entity.vo.GetVerificationCodeVO;

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
}
