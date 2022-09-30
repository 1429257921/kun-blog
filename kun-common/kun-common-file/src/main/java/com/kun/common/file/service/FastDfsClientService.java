package com.kun.common.file.service;

import cn.hutool.core.util.StrUtil;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.exception.FdfsServerException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.kun.common.core.exception.Assert;
import com.kun.common.core.exception.BizException;
import com.kun.common.core.utils.io.KunIoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * FastDFS客户端服务类
 *
 * @author gzc
 * @since 2022/9/30 20:36
 */
@RequiredArgsConstructor
public class FastDfsClientService {

    private final FastFileStorageClient storageClient;


    @Value("${fdfs.groupName:#{null}")
    private String groupName;

    /**
     * 文件上传
     *
     * @param groupName 卷名
     * @param fileBytes 文件字节数组
     * @return 存储路径
     */
    public String upload(String groupName, byte[] fileBytes, String fileType) {
        try {
            Assert.notBlank(groupName, "卷名为空");
            Assert.notNullElements(fileBytes, "上传文件为空");
            fileType = StrUtil.isBlank(fileType) ? KunIoUtil.getFileType(fileBytes) : fileType;
            Assert.notBlank(fileType, "文件类型为空");
            StorePath storePath = storageClient
                    .uploadFile(groupName, new ByteArrayInputStream(fileBytes), fileBytes.length, fileType);
            return storePath.getFullPath();
        } catch (Exception e) {
            throw new BizException("FastDFS上传文件失败", e);
        }
    }


    /**
     * 删除文件
     *
     * @param remoteFilePath fastDfs上文件存储的路径
     */
    public void delFile(String remoteFilePath) {
        storageClient.deleteFile(remoteFilePath);
    }

    /**
     * 下载 示例: yunlianqian/M0/00/00/LAKDJLAFNLNlksjdal.png
     *
     * @param groupName      卷名 示例: yunlianqian
     * @param remoteFilePath fastDfs上文件存储的路径  示例: M0/00/00/LAKDJLAFNLNlksjdal.png
     * @return
     */
    public byte[] download(String groupName, String remoteFilePath) {
        try {
            InputStream ins = storageClient.downloadFile(groupName, remoteFilePath, ins1 -> {
                // 将此ins返回给上面的ins
                return ins1;
            });
            return KunIoUtil.cloneInputStream(ins).toByteArray();
        } catch (FdfsServerException e) {
            if (e.getMessage().indexOf("找不到节点或文件") == 1) {
                throw new BizException("FastDFS文件下载失败,未找到文件,请重新上传!", e);
            }
            throw e;
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public FastFileStorageClient getStorageClient() {
        return storageClient;
    }
}