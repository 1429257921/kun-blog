package com.kun.blog.service.impl;

import com.kun.blog.entity.po.User;
import com.kun.blog.entity.po.UserAuth;
import com.kun.blog.entity.req.SetupPassWordReq;
import com.kun.blog.enums.UserAuthLoginTypeEnum;
import com.kun.blog.mapper.UserMapper;
import com.kun.blog.security.dto.JwtUser;
import com.kun.blog.service.IUserAuthService;
import com.kun.blog.service.IUserService;
import com.kun.blog.util.SecurityUtils;
import com.kun.common.core.exception.Assert;
import com.kun.common.core.exception.BizException;
import com.kun.common.database.service.impl.BaseServiceImpl;
import com.kun.common.redis.constants.CacheConstants;
import com.kun.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gzc
 * @since 2023-01-05 17:59:28
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements IUserService {

    private final IUserAuthService userAuthService;
    private final RedisService redisService;

    @Override
    public User getByPhone(String phone) {
        return lambdaQuery().eq(User::getPhone, phone).one();
    }

    @Override
    public void setupPassword(SetupPassWordReq req) {
        JwtUser jwtUser = (JwtUser) SecurityUtils.getUserDetails();
        // 校验验证码
        validatedPhoneCode(jwtUser.getPhone(), req.getCode());
        //  密码rsa解密
//        String passWord = RsaUtil.decryptByPrivateKey(setupPassWordReq.getPassword());
//        if (!ReUtil.isMatch(RegConstant.USER_PASS_WORD_REG, passWord)) {
//            throw new BizException("密码格式错误！");
//        }
        // 密码md5加密存到数据库
//        String encodePassWord = passwordEncoder.encode(passWord);

        UserAuth userAuth = userAuthService.getByUserId(jwtUser.getId(), UserAuthLoginTypeEnum.ACCOUNT);
        if (userAuth == null) {
            userAuth = UserAuth.builder()
                    .account(jwtUser.getPhone())
                    .loginType(UserAuthLoginTypeEnum.ACCOUNT.getCode())
                    .userId(jwtUser.getId())
                    .build();
        }
        userAuth.setPassword(req.getPassword());
        Assert.isTrue(userAuthService.saveOrUpdate(userAuth), "插入或更新登录授权信息失败");
        // 删除手机短信验证码
        redisService.deleteObject(CacheConstants.SMS_AUTH_CODE + jwtUser.getPhone());
    }

    /**
     * 校验验证码
     *
     * @param phone 手机号
     * @param code  验证码
     */
    private void validatedPhoneCode(String phone, Integer code) {
        String redisKey = CacheConstants.SMS_AUTH_CODE + phone;
        Integer redisCode = redisService.getCacheObject(redisKey);
        if (redisCode == null) {
            throw new BizException("验证码无效");
        }
        if (!redisCode.equals(code)) {
            throw new BizException("验证码错误");
        }
    }
}
