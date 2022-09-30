package com.kun.common.http.config;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp配置类
 *
 * @author: gzc
 * @createTime: 2022-3-11 4:55
 * @since: 1.0
 **/
@Slf4j
@RequiredArgsConstructor
public class OkHttpConfiguration {

    /**
     * 连接超时时间(秒)
     */
    private static long DEFAULT_CONNECT_TIMEOUT = 60;
    /**
     * 读取超时时间(秒)
     */
    private static long DEFAULT_READ_TIMEOUT = 60;
    /**
     * 写入超时时间(秒)
     */
    private static long DEFAULT_WRITE_TIMEOUT = 60;
    /**
     * 连接池中整体的空闲连接的最大数量
     */
    private static int DEFAULT_MAX_IDLE_CONNECTIONS = 100;
    /**
     * 连接空闲时间最多为 300 秒
     */
    private static long DEFAULT_KEEPALIVE_DURATION = 250L;

    private final ConfigurableEnvironment configurableEnvironment;

    @SuppressWarnings("all")
    @Bean
    public OkHttpClient okHttpClient() {
        String connectTimeout = configurableEnvironment.getProperty("ok.http.connect-timeout");
        String readTimeout = configurableEnvironment.getProperty("ok.http.read-timeout");
        String writeTimeout = configurableEnvironment.getProperty("ok.http.write-timeout");
        log.info("okHttpConfiguration配置信息connectTimeout->{},readTimeout->{},writeTimeout->{}",
                connectTimeout, readTimeout, writeTimeout);
        return new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory(), x509TrustManager())
                // 是否开启缓存
                .retryOnConnectionFailure(false)
                .connectionPool(pool())
                .connectTimeout(
                        StrUtil.isBlank(connectTimeout) ? DEFAULT_CONNECT_TIMEOUT : Long.valueOf(connectTimeout),
                        TimeUnit.SECONDS)
                .readTimeout(StrUtil.isBlank(readTimeout) ? DEFAULT_READ_TIMEOUT : Long.valueOf(readTimeout),
                        TimeUnit.SECONDS)
                .writeTimeout(StrUtil.isBlank(writeTimeout) ? DEFAULT_WRITE_TIMEOUT : Long.valueOf(writeTimeout),
                        TimeUnit.SECONDS)
                .hostnameVerifier((hostname, session) -> true)
                // 设置代理
//            	.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888)))
                // 拦截器
//                .addInterceptor()
                .build();
    }

    @Bean
    public X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    @SuppressWarnings("all")
    @Bean
    public SSLSocketFactory sslSocketFactory() {
        try {
            // 信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.error("创建sslSocketFactory发生密钥异常, 异常原因:{}", e);
        }
        return null;
    }

    @Bean
    public ConnectionPool pool() {
        String maxIdleConnections = configurableEnvironment.getProperty("ok.http.max-idle-connections");
        String keepAliveDuration = configurableEnvironment.getProperty("ok.http.keep-alive-duration");
//		System.out.println(StrUtil.format("maxIdleConnections->{},keepAliveDuration->{}",
//				maxIdleConnections, keepAliveDuration));
        return new ConnectionPool(
                StrUtil.isBlank(maxIdleConnections) ? DEFAULT_MAX_IDLE_CONNECTIONS
                        : Integer.valueOf(maxIdleConnections),
                StrUtil.isBlank(keepAliveDuration) ? DEFAULT_KEEPALIVE_DURATION
                        : Long.valueOf(keepAliveDuration),
                TimeUnit.SECONDS);
    }
}
