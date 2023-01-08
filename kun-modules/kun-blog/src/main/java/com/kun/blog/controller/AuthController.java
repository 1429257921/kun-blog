package com.kun.blog.controller;

import com.kun.blog.anno.AnonymousAccess;
import com.kun.blog.entity.req.*;
import com.kun.blog.entity.vo.GetVerificationCodeVO;
import com.kun.blog.entity.vo.SendPhoneCodeVO;
import com.kun.blog.entity.vo.UserLoginVO;
import com.kun.blog.entity.vo.UserRegisterVO;
import com.kun.blog.service.AuthService;
import com.kun.common.core.exception.Assert;
import com.kun.common.log.anno.APIMessage;
import com.kun.common.redis.aop.Limit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;

/**
 * token控制层
 *
 * @author gzc
 * @since 2022/10/7 17:35
 **/
@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 获取图形验证码
     *
     * @author gzc
     * @since 2022/10/12 1:22
     */
    @Limit(period = 2, count = 1)
    @APIMessage(value = "获取验证码", printReqParam = false)
    @AnonymousAccess
    @GetMapping(value = "getCode")
    public Callable<ResponseEntity<GetVerificationCodeVO>> getCode() {
        return () -> ResponseEntity.ok(authService.getCode());
    }


    /**
     * 校验图形验证码
     *
     * @author gzc
     * @since 2022/10/13 3:37
     */
    @APIMessage("校验图形验证码")
    @AnonymousAccess
    @PostMapping(value = "validatedCode")
    public ResponseEntity<Object> validatedCode(@RequestBody @Validated ValidatedCodeReq validatedCodeReq) {
        authService.validatedCode(validatedCodeReq);
        return ResponseEntity.ok().build();
    }

    /**
     * 发送手机验证码
     *
     * @param phone 手机号
     */
    @Limit(period = 2, count = 1)
    @APIMessage(value = "发送手机验证码", printReqParam = false)
    @AnonymousAccess
    @GetMapping(value = "sendPhoneCode")
    public Callable<ResponseEntity<SendPhoneCodeVO>> sendPhoneCode(@RequestParam String phone) {
        return () -> {
            Assert.notBlank(phone, "手机号为空");
            return ResponseEntity.ok(authService.sendPhoneCode(phone));
        };
    }

    /**
     * 校验手机验证码
     *
     * @author gzc
     * @since 2022/10/13 3:37
     */
    @APIMessage("校验手机验证码")
    @AnonymousAccess
    @PostMapping("validatedPhoneCode")
    public ResponseEntity<Object> validatedPhoneCode(@RequestBody @Validated ValidatedPhoneCodeReq validatedCodeReq) {
        authService.validatedPhoneCode(validatedCodeReq);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 用户登录
     *
     * @author gzc
     * @since 2022/10/12 1:22
     */
    @Limit(period = 3, count = 1)
    @APIMessage("用户登录")
    @AnonymousAccess
    @PostMapping("login")
    public ResponseEntity<UserLoginVO> login(@RequestBody @Validated UserLoginReq userLoginReq) throws Exception {
        return ResponseEntity.ok(authService.login(userLoginReq));
    }

    /**
     * 用户注册
     *
     * @author gzc
     * @since 2022/10/16 1:20
     */
    @Limit(period = 3, count = 1)
    @AnonymousAccess
    @APIMessage("用户注册")
    @PostMapping("register")
    public ResponseEntity<UserRegisterVO> register(@RequestBody @Validated UserRegisterReq userRegisterReq) {
        return ResponseEntity.ok(authService.register(userRegisterReq));
    }



}

