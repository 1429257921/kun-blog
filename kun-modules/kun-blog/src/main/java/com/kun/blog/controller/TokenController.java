package com.kun.blog.controller;

import com.kun.blog.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * token控制层
 *
 * @author gzc
 * @since 2022/10/7 17:35
 **/
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/token/")
public class TokenController {

    private final TokenService tokenService;


}

