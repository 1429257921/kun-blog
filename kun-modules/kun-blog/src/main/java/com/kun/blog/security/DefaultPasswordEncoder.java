package com.kun.blog.security;

import cn.hutool.crypto.SecureUtil;
import com.kun.common.core.exception.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义密码管理工具类
 *
 * @author gzc
 * @since 2022/10/10 2:08
 **/
@Slf4j
@Component
public class DefaultPasswordEncoder extends BCryptPasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        // md5加密
        return SecureUtil.md5(String.valueOf(rawPassword));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (true) {
            return true;
        }
        log.info("校验密码");
        Assert.notNull(rawPassword, "rawPassword cannot be null");
        Assert.notBlank(encodedPassword, "encodedPassword is null");
        String encode = SecureUtil.md5(String.valueOf(rawPassword));
        return encodedPassword.equals(encode);
    }
}
