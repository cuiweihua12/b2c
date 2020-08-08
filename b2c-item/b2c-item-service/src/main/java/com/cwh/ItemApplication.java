package com.cwh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-07-13 19:01
 */
@SpringBootApplication
@EnableEurekaClient
@EnableCaching
public class ItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class);
    }
}
