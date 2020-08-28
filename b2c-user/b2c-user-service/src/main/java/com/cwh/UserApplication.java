package com.cwh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-25 21:48
 */
@SpringBootApplication
@EnableEurekaClient
@EnableScheduling //开启定时任务
@MapperScan("com.cwh.mapper")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class);
    }
}
