package com.kun.blog.controller;

import com.kun.blog.service.IKunUserService;
import com.kun.common.log.anno.APIMessage;
import com.kun.common.redis.aop.Limit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息接口控制层
 *
 * @author gzc
 * @since 2022/10/7 17:47
 **/
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/")
public class KunUserController {

    private final IKunUserService iKunUserService;

    /**
     * 获取个人信息
     *
     * @author gzc
     * @since 2022/10/19 5:01
     */
    @Limit
    @APIMessage(value = "获取个人信息", printReqParam = false)
    @GetMapping("getUserDetails")
    public ResponseEntity<Object> getUserDetails() {
        return new ResponseEntity<>(iKunUserService.getUserDetails(), HttpStatus.OK);
    }

}
