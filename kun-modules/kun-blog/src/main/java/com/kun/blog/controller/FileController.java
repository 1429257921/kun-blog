package com.kun.blog.controller;

import com.kun.blog.service.IKunFileService;
import com.kun.common.log.anno.APIMessage;
import com.kun.common.redis.aop.Limit;
import com.kun.common.redis.constants.CacheConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件控制层
 *
 * @author gzc
 * @since 2022/10/18 19:48
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file/")
public class FileController {

    private final IKunFileService kunFileService;

    /**
     * 上传文件
     *
     * @author gzc
     * @since 2022/10/6 13:12
     */
    @Limit(prefix = CacheConstants.PC_LIMIT_PREFIX)
    @APIMessage(value = "上传文件", printReqParam = false)
    @RequestMapping("upload")
    public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file) throws Exception {
        return new ResponseEntity<>(kunFileService.upload(file), HttpStatus.OK);
    }
}
