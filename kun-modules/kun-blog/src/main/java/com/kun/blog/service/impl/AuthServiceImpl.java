package com.kun.blog.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.kun.blog.entity.req.UserLoginReq;
import com.kun.blog.entity.req.ValidatedCodeReq;
import com.kun.blog.entity.vo.GetVerificationCodeVO;
import com.kun.blog.entity.vo.UserLoginVO;
import com.kun.blog.security.JwtProperties;
import com.kun.blog.security.dto.JwtUser;
import com.kun.blog.service.AuthService;
import com.kun.blog.service.JwtTokenService;
import com.kun.common.core.exception.BizException;
import com.kun.common.redis.service.RedisService;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author gzc
 * @since 2022/10/7 18:19
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RedisService redisService;
    private final UserDetailsService userDetailsService;
    private final JwtProperties jwtProperties;
    private final JwtTokenService tokenUtil;

    @Override
    public GetVerificationCodeVO getCode() {
        // 算术类型 https://gitee.com/whvse/EasyCaptcha
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        // 获取运算的结果
        String result;
        try {
            result = new Double(Double.parseDouble(captcha.text())).intValue() + "";
        } catch (Exception e) {
            result = captcha.text();
        }
        String uuid = jwtProperties.getCodeKey() + IdUtil.simpleUUID();
        // 保存
        redisService.setCacheObject(uuid, result, 2L, TimeUnit.MINUTES);
        // 验证码信息
        GetVerificationCodeVO getVerificationCodeVO = new GetVerificationCodeVO();
        getVerificationCodeVO.setUuid(uuid);
        getVerificationCodeVO.setImg(captcha.toBase64());
        return getVerificationCodeVO;
    }

    @Override
    public void validatedCode(ValidatedCodeReq codeReq) {
        String code = redisService.getCacheObject(codeReq.getUuid());
        if (StrUtil.isBlank(code) || StrUtil.isBlank(codeReq.getCode()) || !code.equals(codeReq.getCode())) {
            throw new BizException("验证码错误");
        }
    }

    @Override
    public UserLoginVO login(UserLoginReq userLoginReq) {
        log.info("进入登录接口");
        UserLoginVO userLoginVO = new UserLoginVO();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userLoginReq.getUserName(), userLoginReq.getPassWord());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 生成令牌
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginReq.getUserName());
        String token = tokenUtil.generateToken(userDetails);
        // 返回 token 与 用户信息
//        userLoginVO.setUser((JwtUser) userDetails);
        userLoginVO.setUserToken(jwtProperties.getTokenStartWith() + token);
        return userLoginVO;
    }


}
