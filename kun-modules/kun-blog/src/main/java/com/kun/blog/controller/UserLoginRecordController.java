package com.kun.blog.controller;

import com.kun.blog.service.IUserLoginRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 坤坤云用户登录记录表控制层
 *
 * @author gzc
 * @since 2023-01-05 18:04:37
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class UserLoginRecordController {

    private final IUserLoginRecordService userLoginRecordService;



}
