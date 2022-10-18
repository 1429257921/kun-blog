package com.kun.blog.controller;

import com.kun.blog.service.ConfigService;
import com.kun.common.log.anno.APIMessage;
import com.kun.common.redis.aop.Limit;
import com.kun.common.redis.aop.LimitType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 配置信息接口控制层
 *
 * @author gzc
 * @since 2022/10/18 1:38
 **/
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/config/")
public class ConfigController {

    private final ConfigService configService;

    /**
     * 获取配置
     *
     * @author gzc
     * @since 2022/10/18 3:12
     */
    @Limit(limitType = LimitType.CUSTOMER)
    @APIMessage(value = "获取配置", printReqParam = false)
    @GetMapping("get")
    public ResponseEntity<Object> getConfig() {
        return new ResponseEntity<>(configService.getConfig(), HttpStatus.OK);
    }
}
