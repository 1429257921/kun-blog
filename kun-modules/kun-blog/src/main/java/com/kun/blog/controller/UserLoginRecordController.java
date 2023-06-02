package com.kun.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kun.blog.entity.req.UserLoginRecordPageListReq;
import com.kun.blog.entity.vo.UserLoginRecordPageListVO;
import com.kun.blog.service.IUserLoginRecordService;
import com.kun.common.database.entity.dto.PageParameter;
import com.kun.common.log.anno.APIMessage;
import com.kun.common.redis.aop.Limit;
import com.kun.common.redis.aop.LimitType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 坤坤云用户登录记录表控制层
 *
 * @author gzc
 * @since 2023-01-05 18:04:37
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/login/record/")
public class UserLoginRecordController {

    private final IUserLoginRecordService userLoginRecordService;

    /**
     * 分页查询登录记录
     *
     * @author gzc
     * @since 2022/10/18 3:12
     */
    @Limit(limitType = LimitType.CUSTOMER)
    @APIMessage(value = "分页查询登录记录", printReqParam = false)
    @PostMapping("pageList")
    public ResponseEntity<Page<UserLoginRecordPageListVO>> pageList(@Validated @RequestBody PageParameter<UserLoginRecordPageListReq> pageParameter) {
        return new ResponseEntity<>(userLoginRecordService.pageList(pageParameter), HttpStatus.OK);
    }

}
