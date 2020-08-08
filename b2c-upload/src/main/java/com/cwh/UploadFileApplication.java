package com.cwh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-07-22 10:38
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UploadFileApplication {
    public static void main(String[] args) {
        SpringApplication.run(UploadFileApplication.class);
    }
}
