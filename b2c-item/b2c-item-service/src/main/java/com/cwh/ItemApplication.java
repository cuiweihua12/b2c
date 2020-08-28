package com.cwh;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-07-13 19:01
 */
@SpringBootApplication
@EnableEurekaClient
@EnableCaching
@EnableRabbit
@EnableFeignClients
public class ItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class);
    }
}
