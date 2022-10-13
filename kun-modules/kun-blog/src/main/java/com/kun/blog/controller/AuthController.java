package com.kun.blog.controller;

import com.kun.blog.anno.AnonymousAccess;
import com.kun.blog.entity.req.UserLoginReq;
import com.kun.blog.entity.req.ValidatedCodeReq;
import com.kun.blog.service.AuthService;
import com.kun.common.log.anno.APIMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * token控制层
 *
 * @author gzc
 * @since 2022/10/7 17:35
 **/
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private final AuthService authService;

    /**
     * 获取验证码
     *
     * @author gzc
     * @since 2022/10/12 1:22
     */
    @APIMessage(value = "获取验证码", printReqParam = false)
    @AnonymousAccess
    @GetMapping(value = "getCode")
    public ResponseEntity<Object> getCode() {
        return new ResponseEntity<>(authService.getCode(), HttpStatus.OK);
    }


    /**
     * 校验验证码
     *
     * @author gzc
     * @since 2022/10/13 3:37
     */
    @APIMessage("校验验证码")
    @AnonymousAccess
    @PostMapping(value = "validatedCode")
    public ResponseEntity<Object> validatedCode(@RequestBody @Validated ValidatedCodeReq validatedCodeReq) {
        authService.validatedCode(validatedCodeReq);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 登录
     *
     * @param userLoginReq
     * @author gzc
     * @since 2022/10/12 1:22
     */
    @APIMessage("登录")
    @AnonymousAccess
    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody @Validated UserLoginReq userLoginReq) {
        return new ResponseEntity<>(authService.login(userLoginReq), HttpStatus.OK);
    }

}

