package com.kun.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关启动类
 *
 * @author xuzhanfu
 * @date 2019-10-07 12:13
 **/

@SpringBootApplication
public class KunGatewayApplication.java {
    public static void main(String[] args) {
        SpringApplication.run(KunGatewayApplication.java.class, args);
    }
}
