package com.kun.blog.service.impl;

import cn.hutool.core.util.IdUtil;
import com.kun.blog.config.SecurityProperties;
import com.kun.blog.entity.vo.GetVerificationCodeVO;
import com.kun.blog.service.AuthService;
import com.kun.common.redis.service.RedisService;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final SecurityProperties properties;

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
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        // 保存
        redisService.setCacheObject(uuid, result, 2L, TimeUnit.MINUTES);
        // 验证码信息
        GetVerificationCodeVO getVerificationCodeVO = new GetVerificationCodeVO();
        getVerificationCodeVO.setUuid(uuid);
        getVerificationCodeVO.setImg(captcha.toBase64());
        return getVerificationCodeVO;
    }
}
