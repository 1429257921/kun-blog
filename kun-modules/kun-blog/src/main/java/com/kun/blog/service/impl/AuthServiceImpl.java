package com.kun.blog.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.kun.blog.entity.po.User;
import com.kun.blog.entity.po.UserAuth;
import com.kun.blog.entity.po.UserLoginRecord;
import com.kun.blog.entity.req.UserLoginReq;
import com.kun.blog.entity.req.UserRegisterReq;
import com.kun.blog.entity.req.ValidatedCodeReq;
import com.kun.blog.entity.req.ValidatedPhoneCodeReq;
import com.kun.blog.entity.vo.GetVerificationCodeVO;
import com.kun.blog.entity.vo.SendPhoneCodeVO;
import com.kun.blog.entity.vo.UserLoginVO;
import com.kun.blog.entity.vo.UserRegisterVO;
import com.kun.blog.enums.DeleteFlagEnum;
import com.kun.blog.enums.UserAuthLoginTypeEnum;
import com.kun.blog.enums.UserStatusEnum;
import com.kun.blog.security.JwtProperties;
import com.kun.blog.security.dto.JwtUser;
import com.kun.blog.service.*;
import com.kun.common.core.exception.Assert;
import com.kun.common.core.exception.BizException;
import com.kun.common.core.utils.ip.IPUtil;
import com.kun.common.core.utils.spring.ContextUtil;
import com.kun.common.redis.constants.CacheConstants;
import com.kun.common.redis.service.RedisService;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @author gzc
 * @since 2022/10/7 18:19
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    /**
     * 过期时长(分钟)
     */
    public static final int EXPIRES_TIME = 5;

    private final RedisService redisService;
    private final UserDetailsService userDetailsService;
    private final IUserService userService;
    private final IUserAuthService userAuthService;
    private final IUserLoginRecordService userLoginRecordService;
    private final JwtProperties jwtProperties;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    @Value("${user-init.head-portrait}")
    private String headPortrait;
    @Value("${sms-config.test}")
    private Boolean smsTest;

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
        log.info("验证码->{}", result);
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserRegisterVO register(UserRegisterReq userRegisterReq) {
        // 校验时间戳
        checkTimeStamp(userRegisterReq.getTimestamp());
        // 校验验证码
        checkCode(userRegisterReq.getPhone(), userRegisterReq.getCode());

        // 用户信息入库
        User user = User.builder().phone(userRegisterReq.getPhone()).nickName("用户" + userRegisterReq.getPhone()).headPortrait(headPortrait).status(UserStatusEnum.ACTIVE.getCode()).registerSource(UserAuthLoginTypeEnum.PHONE_CODE.getCode()).deleted((byte) DeleteFlagEnum.ACTIVE.getCode()).createBy("").updateBy("").build();
        try {
            if (!userService.save(user)) {
                throw new BizException("用户信息入库失败");
            }
        } catch (DuplicateKeyException dke) {
            throw new BizException("用户已存在");
        }

        // 用户登录授权信息入库
        UserAuth userAuth = userAuthService.getByUserId(user.getId(), UserAuthLoginTypeEnum.PHONE_CODE);
        if (userAuth == null) {
            userAuth = UserAuth.builder().loginType(UserAuthLoginTypeEnum.PHONE_CODE.getCode()).account(userRegisterReq.getPhone()).userId(user.getId()).build();
            Assert.isTrue(userAuthService.save(userAuth), "用户登录授权信息入库失败");
        }
        // 删除手机短信验证码
        redisService.deleteObject(CacheConstants.SMS_AUTH_CODE + userRegisterReq.getPhone());

        return new UserRegisterVO();
    }

    @Override
    public UserLoginVO login(UserLoginReq userLoginReq) {
        // 校验时间戳
        checkTimeStamp(userLoginReq.getTimestamp());
        // 初始化上下文
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userLoginReq.getPhone(), userLoginReq.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 查询用户信息
        JwtUser jwtUser = (JwtUser) Assert.notNull(userDetailsService.loadUserByUsername(userLoginReq.getPhone()), "用户不存在");

        // 校验授权信息
        UserAuth userAuth = Assert.notNull(getUserAuth(userLoginReq, jwtUser), "用户授权信息为空");

        // 生成令牌
        String token = jwtTokenService.generateToken(jwtUser);
        log.info("手机号->{}, 生成令牌->{}", userLoginReq.getPhone(), token);
        // 删除手机短信验证码
        redisService.deleteObject(CacheConstants.SMS_AUTH_CODE + userLoginReq.getPhone());
        // 返回 token 与 用户信息
        UserLoginVO vo = UserLoginVO.builder().token(jwtProperties.getTokenStartWith() + token)
                .user(jwtUser)
                .expireTime(jwtProperties.getTokenValidityInSeconds())
                .build();

        UserLoginRecord userLoginRecord = UserLoginRecord.builder()
                .authId(userAuth.getId())
                .loginStatus(userAuth.getLoginType())
                .userId(userAuth.getUserId())
                .ip(IPUtil.getIp(ContextUtil.getRequest()))
                .os("").osVersion("").appVersion("").mac("")
                .build();
        Assert.isTrue(userLoginRecordService.save(userLoginRecord), "插入用户登录记录失败");
        return vo;
    }

    private UserAuth getUserAuth(UserLoginReq userLoginReq, JwtUser jwtUser) {
        UserAuth userAuth;
        // 手机号验证码登录
        if (UserAuthLoginTypeEnum.PHONE_CODE.getCode() == userLoginReq.getType()) {
            // 校验验证码
            checkCode(userLoginReq.getPhone(), userLoginReq.getCode());
            // 查询用户授权信息
            userAuth = userAuthService.getByUserId(jwtUser.getId(), UserAuthLoginTypeEnum.PHONE_CODE);
            if (userAuth == null) {
                userAuth = UserAuth.builder().userId(jwtUser.getId()).account(jwtUser.getPhone()).loginType(UserAuthLoginTypeEnum.PHONE_CODE.getCode()).build();
                Assert.isTrue(userAuthService.save(userAuth), "插入用户授权信息失败");
            }
        }
        // 手机号一键登录
        else if (UserAuthLoginTypeEnum.PHONE_KEY.getCode() == userLoginReq.getType()) {
            userAuth = userAuthService.getByUserId(jwtUser.getId(), UserAuthLoginTypeEnum.PHONE_KEY);
            if (userAuth == null) {
                userAuth = UserAuth.builder().userId(jwtUser.getId()).account(jwtUser.getPhone()).loginType(UserAuthLoginTypeEnum.PHONE_KEY.getCode()).build();
                Assert.isTrue(userAuthService.save(userAuth), "插入用户授权信息失败");
            }
        }
        // 账号密码登录
        else if (UserAuthLoginTypeEnum.ACCOUNT.getCode() == userLoginReq.getType()) {
            Assert.notBlank(userLoginReq.getPassword(), "密码为空");
            // rsa解密
//            String password = RsaUtil.decryptByPrivateKey(userLoginReq.getPassword());
            // 查询登录授权信息
            userAuth = Assert.notNull(userAuthService.getByUserId(jwtUser.getId(), UserAuthLoginTypeEnum.ACCOUNT), "请先设置密码");
            // 校验密码
//            if (!passwordEncoder.matches(password, userAuth.getPassword())) {
//                throw new BizException("密码错误！");
//            }
            if (!userAuth.getPassword().equals(userLoginReq.getPassword())) {
                throw new BizException("密码错误！");
            }
            if (!userLoginReq.getPhone().equals(userAuth.getAccount())) {
                throw new BizException("账号错误!");
            }
        } else {
            throw new BizException("登录类型不匹配");
        }
        return userAuth;
    }

    @Override
    public SendPhoneCodeVO sendPhoneCode(String phone) {
        Integer code = null;
        if (smsTest) {
            code = 1234;
            redisService.setCacheObject(CacheConstants.SMS_AUTH_CODE + phone, code, 5L, TimeUnit.MINUTES);
        }

        return SendPhoneCodeVO.builder().code(code).build();
    }

    @Override
    public void validatedPhoneCode(ValidatedPhoneCodeReq validatedCodeReq) {
        String redisKey = CacheConstants.SMS_AUTH_CODE + validatedCodeReq.getPhone();
        Integer code = redisService.getCacheObject(redisKey);
        if (code == null) {
            throw new BizException("验证码无效");
        }
        if (!validatedCodeReq.getCode().equals(code)) {
            throw new BizException("验证码错误");
        }
    }

    /**
     * 校验时间戳
     *
     * @param timeStamp 时间戳(毫秒)
     */
    private void checkTimeStamp(Long timeStamp) {
        long between = DateUtil.between(DateUtil.date(timeStamp), DateUtil.date(), DateUnit.MINUTE);
        if (between > EXPIRES_TIME) {
            throw new BizException("时间戳已过期");
        }
    }

    /**
     * 校验手机短信验证码
     *
     * @param phone 手机号
     * @param code  短信验证码
     */
    private void checkCode(String phone, Integer code) {
        ValidatedPhoneCodeReq validatedPhoneCodeReq = ValidatedPhoneCodeReq.builder().phone(phone).code(code).build();
        this.validatedPhoneCode(validatedPhoneCodeReq);
    }

}
