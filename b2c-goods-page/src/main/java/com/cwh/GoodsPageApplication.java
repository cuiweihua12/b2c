package com.cwh;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-17 19:57
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients
@EnableRabbit
public class GoodsPageApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsPageApplication.class);
    }
}
