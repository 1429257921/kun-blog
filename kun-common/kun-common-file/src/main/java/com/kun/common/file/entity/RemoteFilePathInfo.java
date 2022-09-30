package com.kun.common.file.entity;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.kun.common.core.exception.Assert;
import com.kun.common.core.exception.BizException;
import com.kun.common.file.enums.FileSystemEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件路径信息
 *
 * @author gzc
 * @since 2022/9/30 20:35
 */
@Slf4j
@Data
public class RemoteFilePathInfo {

    /**
     * 文件名称 示例: aaa
     */
    private String fileName;
    /**
     * 文件类型 示例: png
     */
    private String fileType;
    /**
     * 文件名称加扩展名 示例: aaa.png
     */
    private String fileNameAndType;
    /**
     * 父路径 示例: xxx/xxx/xxx
     */
    private String parentPath;
    /**
     * 卷名 示例: yunlianqian
     */
    private String groupName;

    /**
     * 截取卷名后的路径 示例 xxx/xxx/aaa.png
     */
    private String subPath;

    public static Builder builder(String filePath) {
        return new Builder(filePath);
    }

    /**
     * Builder类
     */
    public static class Builder {

        /**
         * 文件路径
         */
        private String filePath;

        /**
         * 文件服务器类型
         */
        private FileSystemEnum fileSystemEnum;


        public Builder(String filePath) {
            this.fileSystemEnum = FileSystemEnum.FTP;
            this.filePath = filePath;
        }

        public Builder fastDFS() {
            this.fileSystemEnum = FileSystemEnum.FASTDFS;
            return this;
        }

        public RemoteFilePathInfo build() {
            try {
                log.info("构建RemoteFilePathInfo对象 文件路径->{}", this.filePath);
                RemoteFilePathInfo remoteFilePathInfo = new RemoteFilePathInfo();
                Assert.notBlank(this.filePath, "文件路径为空");
                int index = this.filePath.indexOf("/");
                if (index == -1) {
                    throw new BizException("文件路径格式不正确");
                }
                if (index == 0) {
                    this.filePath = StrUtil.subSuf(this.filePath, 1);
                }

                int lastIndex = this.filePath.lastIndexOf("/");
                if (lastIndex == -1) {
                    throw new BizException("文件路径格式不正确");
                }

                // 获取父目录
                String parentPath = StrUtil.subPre(this.filePath, lastIndex);
                remoteFilePathInfo.setParentPath(parentPath);
                // 获取文件名称
                String fileNameAndType = StrUtil.subSuf(this.filePath, lastIndex + 1);
                remoteFilePathInfo.setFileNameAndType(fileNameAndType);
                if (StrUtil.isNotBlank(fileNameAndType)) {
                    String[] splitToArray = StrUtil.splitToArray(fileNameAndType, ".");
                    if (splitToArray.length == 1) {
                        remoteFilePathInfo.setFileName(splitToArray[0]);
                    }
                    if (splitToArray.length == 2) {
                        remoteFilePathInfo.setFileName(splitToArray[0]);
                        remoteFilePathInfo.setFileType(splitToArray[1]);
                    }
                }
                if (FileSystemEnum.FASTDFS.equals(fileSystemEnum)) {
                    // 默认使用FastDFS

                    // 获取卷名
                    remoteFilePathInfo.setGroupName(StrUtil.subPre(this.filePath, index));
                    remoteFilePathInfo.setSubPath(StrUtil.subSuf(this.filePath, index + 1));
                }
                return remoteFilePathInfo;
            } catch (BizException e) {
                throw new BizException("远程文件路径解析错误！", e);
            }
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
