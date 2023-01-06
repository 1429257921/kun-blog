package com.kun.blog.controller;

import com.kun.blog.entity.req.SetupPassWordReq;
import com.kun.blog.service.IUserService;
import com.kun.common.log.anno.APIMessage;
import com.kun.common.redis.aop.Limit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 坤坤云用户表控制层
 *
 * @author gzc
 * @since 2023-01-06 15:00:03
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/user")
public class UserController {

    private final IUserService userService;

    /**
     * 设置密码
     *
     * @param setupPassWordReq 请求参数
     */
    @Limit(period = 3, count = 1)
    @APIMessage("设置密码")
    @PostMapping("setupPassword")
    public ResponseEntity<Object> setupPassword(@Validated @RequestBody SetupPassWordReq setupPassWordReq) {
        userService.setupPassword(setupPassWordReq);
        return ResponseEntity.ok().build();
    }

}
