package com.kun.blog.controller;

import com.kun.blog.service.IKunUserService;
import lombok.RequiredArgsConstructor;
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

}
