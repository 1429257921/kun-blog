package com.kun.blog.service;

import com.kun.blog.entity.po.KunFile;
import com.kun.blog.entity.vo.FileUploadVO;
import com.kun.common.database.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件信息业务接口
 *
 * @author gzc
 * @since 2022-10-19 02:24:22
 */
public interface IKunFileService extends BaseService<KunFile> {

    /**
     * 文件上传
     *
     * @param file
     * @return
     * @throws Exception
     */
    FileUploadVO upload(MultipartFile file) throws Exception;
}
