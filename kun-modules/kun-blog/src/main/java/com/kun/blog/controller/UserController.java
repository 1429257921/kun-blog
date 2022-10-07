package com.kun.blog.controller;

import com.kun.blog.entity.dto.UserLoginReq;
import com.kun.blog.service.IKunUserService;
import com.kun.common.log.anno.APIMessage;
import com.kun.common.redis.aop.Limit;
import com.kun.common.redis.aop.NoRepeatSubmit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户接口控制层
 *
 * @author gzc
 * @since 2022/10/7 17:47
 **/
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/")
public class UserController {
    private final IKunUserService iKunUserService;


    @Limit
    @NoRepeatSubmit
    @APIMessage("用户登录")
    @PostMapping("login")
    public ResponseEntity<Object> login(@Validated UserLoginReq userLoginReq) {
        return new ResponseEntity<>(iKunUserService.login(userLoginReq), HttpStatus.OK);
    }

}
