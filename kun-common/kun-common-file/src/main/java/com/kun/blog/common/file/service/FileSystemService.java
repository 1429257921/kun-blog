package com.kun.blog.common.file.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.kun.blog.common.core.exception.Assert;
import com.kun.blog.common.core.exception.BizException;
import com.kun.blog.common.core.utils.file.KunPathUtil;
import com.kun.blog.common.core.utils.io.KunIoUtil;
import com.kun.blog.common.file.entity.RemoteFilePathInfo;
import com.kun.blog.common.file.enums.FileSystemEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * 统一文件上传下载服务
 *
 * @author gzc
 * @since 2022/9/30 20:36
 */
@Slf4j
@RequiredArgsConstructor
public class FileSystemService {

    private final FastDfsClientService fastDfsClientService;
    private final FtpService ftpService;

    /**
     * 文件上传
     *
     * @param fileSystemEnum  文件系统类型枚举 可为空，默认上传到FastDFS
     * @param fileNameAndType 文件名称 示例: aaa.png
     * @param parentPath      文件存储路径 示例(xxx/xxx/aaa.png) 不为空
     *                        当文件系统类型为fastdfs时,remoteFilePath为卷名 示例:yunlianqian
     * @param fileBytes       文件字节数组 不为null
     * @return true 上传成功
     * @throws BizException
     */
    public String upload(FileSystemEnum fileSystemEnum, String fileNameAndType,
                         String parentPath, byte[] fileBytes) throws BizException {
        long beginDate = System.currentTimeMillis();
        try {
            log.info("文件服务器类型->{},文件上传路径->{},文件字节大小->{}",
                    fileSystemEnum.name(), parentPath, Optional.ofNullable(fileBytes).orElse(new byte[0]).length);
            Assert.notBlank(parentPath, "远程文件路径为空");
            Assert.notNullElements(fileBytes, "文件字节流为空");
            Assert.notBlank(fileNameAndType, "文件名称为空");
            if (FileSystemEnum.FTP.equals(fileSystemEnum)) {
//				return ftpUtil.upload(fileNameAndType, parentPath, MyIoUtil.getInputStream(fileBytes));
                return ftpService.upload(fileNameAndType, parentPath, KunIoUtil.getInputStream(fileBytes));
            }
            // 默认使用FastDFS
            return fastDfsClientService.upload(fastDfsClientService.getGroupName(), fileBytes, KunPathUtil.getFileType(fileNameAndType));
        } catch (BizException be) {
            throw new BizException("文件上传失败," + be.getMessage(), be);
        } catch (Exception e) {
            throw new BizException("文件上传失败", e);
        } finally {
            log.info("文件上传到{}耗时(ms)->{}", fileSystemEnum.name(), System.currentTimeMillis() - beginDate);
        }
    }

    /**
     * 上传PDF文件
     *
     * @param fileSystemEnum 文件服务器类型
     * @param fileBytes      文件字节数组
     * @return 上传成功 返回存储路径
     * @throws BizException
     */
    public String uploadPDF(FileSystemEnum fileSystemEnum, byte[] fileBytes) throws BizException {
        // 校验文件类型是否是PDF
        String fileType = KunIoUtil.getFileType(fileBytes);
        if (StrUtil.isBlank(fileType)) {
            throw new BizException("上传PDF文件失败,无法获取文件类型,请排查上传文件是否正确!");
        }
        if (!"pdf".equalsIgnoreCase(fileType)) {
            throw new BizException("上传PDF文件失败,文件非PDF类型,请排查上传文件是否正确!");
        }
        // 示例: 3e8f55a657bd4bd9b04c8020840a65a3.pdf
        String fileNameAndType = UUID.fastUUID().toString(true) + ".pdf";
        // 格式:yyy-MM-dd 示例: 2022-06-18
        String today = DateUtil.today();
        // 示例: PDF/2022-06-18
        String parentPath = FtpService.PDF_ROOT_PATH + today;
        return upload(fileSystemEnum, fileNameAndType, parentPath, fileBytes);
    }

    /**
     * 上传图片文件
     *
     * @param fileSystemEnum 文件服务器类型
     * @param fileBytes      文件字节数组
     * @return 上传成功 返回存储路径
     * @throws BizException
     */
    public String uploadImg(FileSystemEnum fileSystemEnum, byte[] fileBytes) throws BizException {
        // 校验文件类型是否是PDF
        String fileType = KunIoUtil.getFileType(fileBytes);
        if (StrUtil.isBlank(fileType)) {
            throw new BizException("上传图片文件失败,无法获取图片文件类型,请排查上传文件是否正确!");
        }
        if (!"png".equalsIgnoreCase(fileType) && !"jpg".equalsIgnoreCase(fileType)) {
            throw new BizException("上传图片文件失败,文件非PNG或者JPG类型,请排查上传文件格式是否正确!");
        }

        // 示例: 3e8f55a657bd4bd9b04c8020840a65a3.pdf
        String fileNameAndType = UUID.fastUUID().toString(true) + "." + fileType;
        // 格式:yyy-MM-dd 示例: 2022-06-18
        String today = DateUtil.today();
        // 示例: PDF/2022-06-18
        String parentPath = FtpService.IMG_ROOT_PATH + today;
        return upload(fileSystemEnum, fileNameAndType, parentPath, fileBytes);
    }

    /**
     * 文件上传
     *
     * @param fileSystemEnum 文件系统类型枚举 可为空，默认上传到FastDFS
     * @param remoteFilePath 文件存储路径 示例(xxx/xxx/aaa.png) 不为空
     *                       当文件系统类型为fastdfs时,remoteFilePath为卷名 示例:yunlianqian
     * @param fileBytes      文件字节数组 不为null
     * @return true 上传成功
     * @throws BizException
     */
    public String upload(FileSystemEnum fileSystemEnum, String remoteFilePath, byte[] fileBytes)
            throws BizException {
        Assert.notBlank(remoteFilePath, "文件上传路径为空");
        if (FileSystemEnum.FASTDFS.getCode().equals(fileSystemEnum.getCode())) {
            return upload(fileSystemEnum, UUID.fastUUID().toString(true), remoteFilePath, fileBytes);
        }
        RemoteFilePathInfo filePathInfo = RemoteFilePathInfo
                .builder(remoteFilePath)
                .build();
        return upload(fileSystemEnum, filePathInfo.getFileNameAndType(), filePathInfo.getParentPath(), fileBytes);
    }

    /**
     * 文件下载 (根据路径动态区分是FTP的路径还是FastDFS的路径)
     *
     * @param remoteFilePath 服务器文件存储路径: 不为空
     *                       {@link FileSystemEnum#FTP} 路径格式:xxx/xxx/aaa.png
     *                       {@link FileSystemEnum#FASTDFS} 路径格式:组名/xxx/xxx/aaa.png
     * @return 文件字节数据
     * @throws BizException
     */
    public byte[] download(String remoteFilePath) throws Exception {
        FileSystemEnum fileSystemEnum = null;
        long be = System.currentTimeMillis();
        try {
            log.info("开始下载文件");
            Assert.notBlank(remoteFilePath, "文件下载失败,远程文件路径为空");
            // 默认使用FastDFS
            RemoteFilePathInfo pathInfo = RemoteFilePathInfo.builder(remoteFilePath).fastDFS().build();

            if (StrUtil.isNotBlank(pathInfo.getGroupName())
                    && StrUtil.equals(fastDfsClientService.getGroupName(), pathInfo.getGroupName())) {
                log.info("从FastDFS下载文件,文件路径->{}", remoteFilePath);
                // FastDFS下载
                fileSystemEnum = FileSystemEnum.FASTDFS;
                Assert.notBlank(pathInfo.getGroupName(), "FastDFS文件下载失败, 获取卷名称为空");
                Assert.notBlank(pathInfo.getSubPath(), "FastDFS文件下载失败, 文件下载路径格式不正确");
//				return download(fileSystemEnum, remoteFilePath);
                return fastDfsClientService.download(pathInfo.getGroupName(), pathInfo.getSubPath());
            }
            // FTP下载
            log.info("从FTP下载文件,文件路径->{}", remoteFilePath);
            fileSystemEnum = FileSystemEnum.FTP;
//			return download(fileSystemEnum, remoteFilePath);
//			return ftpUtil.download(pathInfo.getFileNameAndType(), pathInfo.getParentPath());
            return ftpService.download(pathInfo.getFileNameAndType(), pathInfo.getParentPath());
        } catch (BizException e) {
            throw new BizException("文件下载失败," + e.getMessage(), e);
        } finally {
            log.info("从{}下载文件耗时(ms)->{}", fileSystemEnum.name(), System.currentTimeMillis() - be);
        }
    }
}
