package com.kun.blog.entity.vo;

import lombok.Data;

/**
 * 文件上传接口响应对象
 *
 * @author gzc
 * @since 2022/10/19 2:26
 **/
@Data
public class FileUploadVO {
    /**
     * 文件ID
     */
    private String fileId;
    /**
     * 文件保存路径
     */
    private String uploadPath;
}
