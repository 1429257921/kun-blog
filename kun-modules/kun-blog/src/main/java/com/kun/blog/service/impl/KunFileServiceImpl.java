package com.kun.blog.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.kun.blog.entity.po.KunFile;
import com.kun.blog.entity.vo.FileUploadVO;
import com.kun.blog.mapper.KunFileMapper;
import com.kun.blog.security.dto.JwtUser;
import com.kun.blog.service.IKunFileService;
import com.kun.common.core.exception.Assert;
import com.kun.common.core.utils.io.KunIoUtil;
import com.kun.common.database.service.impl.BaseServiceImpl;
import com.kun.common.file.enums.FileSystemEnum;
import com.kun.common.file.service.FileSystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author gzc
 * @since 2022-10-19 02:24:22
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KunFileServiceImpl extends BaseServiceImpl<KunFileMapper, KunFile> implements IKunFileService {

    private final FileSystemService fileSystemService;

    @Override
    public FileUploadVO upload(MultipartFile file) throws Exception {
        InputStream fileInputStream = Assert.notNull(file.getInputStream(), "文件流为空");
        ByteArrayOutputStream byteArrayOutputStream =
                Assert.notNull(KunIoUtil.cloneInputStream(fileInputStream), "文件字节流为空");
        int fileLength = byteArrayOutputStream.toByteArray().length;
        log.info("文件大小->{}", fileLength);
        // 获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        // 上传文件
        String uploadPath = fileSystemService.uploadImg(FileSystemEnum.FASTDFS, byteArrayOutputStream.toByteArray());
        KunFile kunFile = new KunFile();
        kunFile.setSize(String.valueOf(fileLength));
        kunFile.setMd5code(SecureUtil.md5(KunIoUtil.getInputStream(byteArrayOutputStream)));
        kunFile.setUrl(uploadPath);
        kunFile.setFileName(file.getOriginalFilename());
        kunFile.setUserName(jwtUser.getUsername());
        // 入库
        Assert.moreThanZero(baseMapper.insert(kunFile), "文件信息入库失败");
        FileUploadVO emojiUploadVO = new FileUploadVO();
        emojiUploadVO.setUploadPath(uploadPath);
        emojiUploadVO.setFileId(String.valueOf(kunFile.getId()));
        return emojiUploadVO;
    }
}
