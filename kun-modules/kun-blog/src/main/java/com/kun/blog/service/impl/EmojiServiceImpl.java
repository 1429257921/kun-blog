package com.kun.blog.service.impl;

import com.github.pagehelper.PageInfo;
import com.kun.blog.entity.dto.EmojiGetConfigReq;
import com.kun.blog.entity.dto.EmojiSearchReq;
import com.kun.blog.entity.po.Emoji;
import com.kun.blog.entity.vo.EmojiGetConfigVO;
import com.kun.blog.entity.vo.EmojiUploadVO;
import com.kun.blog.mapper.EmojiMapper;
import com.kun.blog.service.IEmojiService;
import com.kun.common.core.exception.Assert;
import com.kun.common.core.exception.BizException;
import com.kun.common.core.utils.io.KunIoUtil;
import com.kun.common.database.service.impl.BaseServiceImpl;
import com.kun.common.database.util.QueryHelpPlus;
import com.kun.common.file.enums.FileSystemEnum;
import com.kun.common.file.service.FileSystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author gzc
 * @since 2022-10-01 20:04:43
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EmojiServiceImpl extends BaseServiceImpl<EmojiMapper, Emoji> implements IEmojiService {

    private final FileSystemService fileSystemService;

    @Value("${fdfs.fileServerUrl}")
    private String emojiBaseUrl;

    @Override
    public PageInfo<Emoji> search(EmojiSearchReq emojiSearchReq, Pageable pageable) {
        getPage(pageable);
        return new PageInfo<>(baseMapper.selectList(QueryHelpPlus.getPredicate(Emoji.class, emojiSearchReq)));
    }

    @Override
    public EmojiGetConfigVO getConfig(EmojiGetConfigReq emojiSearchReq) {
        EmojiGetConfigVO configVO = new EmojiGetConfigVO();
        configVO.setEmojiBaseUrl(emojiBaseUrl);
        return configVO;
    }

    @Override
    public EmojiUploadVO upload(MultipartFile file) throws IOException {
        InputStream fileInputStream = Assert.notNull(file.getInputStream(), "文件流为空");
        ByteArrayOutputStream byteArrayOutputStream =
                Assert.notNull(KunIoUtil.cloneInputStream(fileInputStream), "文件字节流为空");
        log.info("文件大小->{}", byteArrayOutputStream.toByteArray().length);
        if (byteArrayOutputStream.toByteArray().length > 5242880) {
            throw new BizException("表情包图片过大");
        }
        // 上传文件
        String uploadPath = fileSystemService.uploadImg(FileSystemEnum.FASTDFS, byteArrayOutputStream.toByteArray());
        EmojiUploadVO emojiUploadVO = new EmojiUploadVO();
        emojiUploadVO.setUploadPath(uploadPath);
        return emojiUploadVO;
    }
}
