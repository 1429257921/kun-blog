package com.kun.blog.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.kun.blog.constant.RegConstant;
import com.kun.blog.entity.po.KunUser;
import com.kun.blog.entity.req.UserLoginReq;
import com.kun.blog.entity.req.UserRegisterReq;
import com.kun.blog.entity.req.ValidatedCodeReq;
import com.kun.blog.entity.vo.GetVerificationCodeVO;
import com.kun.blog.entity.vo.UserLoginVO;
import com.kun.blog.entity.vo.UserRegisterVO;
import com.kun.blog.security.JwtProperties;
import com.kun.blog.security.dto.JwtUser;
import com.kun.blog.service.AuthService;
import com.kun.blog.service.IKunUserService;
import com.kun.blog.service.JwtTokenService;
import com.kun.blog.util.RsaUtil;
import com.kun.common.core.exception.BizException;
import com.kun.common.redis.service.RedisService;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final IKunUserService iKunUserService;
    private final JwtProperties jwtProperties;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

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
        redisService.setCacheObject(uuid, result, 1L, TimeUnit.MINUTES);
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
        // 删除验证码
        redisService.deleteObject(codeReq.getUuid());
    }

    @Override
    public UserRegisterVO register(UserRegisterReq userRegisterReq) {
        if (!ReUtil.isMatch(RegConstant.USER_NAME_REG, userRegisterReq.getUserName())) {
            throw new BizException("账号名格式错误！");
        }
        // 密码rsa解密
        String passWord = RsaUtil.decryptByPrivateKey(userRegisterReq.getPassWord());
        if (!ReUtil.isMatch(RegConstant.USER_PASS_WORD_REG, passWord)) {
            throw new BizException("密码格式错误！");
        }
        // 密码md5加密存到数据库
        String encodePassWord = passwordEncoder.encode(passWord);
        KunUser kunUser = new KunUser();
        kunUser.setUsername(userRegisterReq.getUserName());
        kunUser.setPassword(encodePassWord);
        kunUser.setUserType("0");
        kunUser.setNickname("用户" + DateUtil.currentSeconds());
        try {
            if (!iKunUserService.save(kunUser)) {
                throw new BizException("用户信息入库失败");
            }
        } catch (DuplicateKeyException dke) {
            throw new BizException("用户已存在");
        }
        return null;
    }

    @Override
    public UserLoginVO login(UserLoginReq userLoginReq) {
        // rsa解密
        String passWord = RsaUtil.decryptByPrivateKey(userLoginReq.getPassWord());

        UserLoginVO userLoginVO = new UserLoginVO();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userLoginReq.getUserName(), passWord);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 生成令牌
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginReq.getUserName());
        JwtUser jwtUser = (JwtUser) userDetails;
        // 校验密码
        if (!passwordEncoder.matches(passWord, jwtUser.getPassword())) {
            throw new BizException("密码错误！");
        }

        String token = jwtTokenService.generateToken(userDetails);
        // 返回 token 与 用户信息
        userLoginVO.setUserToken(jwtProperties.getTokenStartWith() + token);
        userLoginVO.setUser(jwtUser);
        userLoginVO.setExpireTime(jwtProperties.getTokenValidityInSeconds());
        return userLoginVO;
    }


}
