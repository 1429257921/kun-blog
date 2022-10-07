package com.kun.blog.controller;

import com.kun.blog.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
public class AuthController {

    private final AuthService authService;

    @GetMapping(value = "/code")
    public ResponseEntity<Object> getCode() {
        return new ResponseEntity<>(authService.getCode(), HttpStatus.OK);
    }

}

