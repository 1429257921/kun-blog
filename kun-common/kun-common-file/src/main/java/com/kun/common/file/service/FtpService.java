package com.kun.common.file.service;

import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpMode;
import com.kun.common.core.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * hutool FTP服务类
 *
 * @author gzc
 * @since 2022/9/30 20:36
 */
@RequiredArgsConstructor
@Slf4j
public class FtpService {

    public static String PDF_ROOT_PATH = "PDF/";
    public static String IMG_ROOT_PATH = "IMG/";

    private final ConfigurableEnvironment configurableEnvironment;

    /**
     * 检查FTP连接是否正常
     *
     * @return 是否连接成功
     */
    public boolean checkConnect() {
        try {
            Ftp build = build();
            build.pwd();
            return true;
        } catch (Exception e) {
            log.error("FTP连接超时,超时原因:{}", e);
        }
        return false;
    }

    /**
     * FTP文件上传
     *
     * @param fileName       文件名称+文件扩展名 示例: aaa.png
     * @param remoteFilePath 远程文件目录 示例: xxx/xxx/xxx
     * @param input          文件输入流
     * @return 文件保存路径 示例: xxx/xxx/xxx/aaa.png
     */
    public String upload(String fileName, String remoteFilePath, InputStream input) {
        Ftp ftp = build();
        checkFilePath(remoteFilePath, ftp);
        boolean upload = ftp.upload(remoteFilePath, fileName, input);
        if (!upload) {
            log.error("上传文件到FTP失败");
            throw new BizException("上传文件到FTP失败");
        }
        return remoteFilePath + "/" + fileName;
    }

    /**
     * FTP文件下载
     *
     * @param fileName       文件名称+文件扩展名 示例: aaa.png
     * @param remoteFilePath 远程文件目录 示例: xxx/xxx/xxx
     * @return 文件字节数组
     */
    public byte[] download(String fileName, String remoteFilePath) {
        Ftp ftp = build();
        checkFilePath(remoteFilePath, ftp);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ftp.download(remoteFilePath, fileName, outputStream);
        return outputStream.toByteArray();
    }


    /**
     * 检查文件目录是否存在，不存在则创建
     *
     * @param remoteFilePath 文件目录
     * @param ftp            {@link Ftp}
     */
    private void checkFilePath(String remoteFilePath, Ftp ftp) {
        if (!ftp.exist(remoteFilePath)) {
            ftp.mkDirs(remoteFilePath);
            if (!ftp.exist(remoteFilePath)) {
                log.error("FTP创建目录失败");
                throw new BizException("FTP创建目录失败");
            }
        }
    }


    /**
     * 构建FTP对象
     *
     * @return {@link Ftp}
     */
    private Ftp build() {
        String userName = configurableEnvironment.getProperty("ftp-config.userName");
        String passWord = configurableEnvironment.getProperty("ftp-config.passWord");
        String host = configurableEnvironment.getProperty("ftp-config.host");
        String port = configurableEnvironment.getProperty("ftp-config.port");
        // 构建FTP
        FtpConfig ftpConfig = new FtpConfig()
                .setHost(host)
                .setPort(Integer.valueOf(port))
                .setUser(userName)
                .setPassword(passWord)
                .setCharset(Charset.forName("utf-8"))
                .setConnectionTimeout(60000)
                .setSoTimeout(60000);
        return new Ftp(ftpConfig, FtpMode.Passive);
    }

}
