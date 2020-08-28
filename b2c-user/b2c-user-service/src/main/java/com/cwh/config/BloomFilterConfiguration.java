package com.cwh.config;

import com.cwh.api.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-25 10:16
 */
@Configuration
public class BloomFilterConfiguration {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.password}")
    private String password;

    private Client client;

    private Logger logger = LoggerFactory.getLogger(BloomFilterConfiguration.class);

    @Bean
    public Client redisBloomFilter(){
        logger.info("redis 连接 初始化");
        client = new Client(host,port,500,100,password);
        return client;
    }

    @PreDestroy
    public void closeRedis(){
        logger.info("redis 连接 销毁");
        client.close();
    }

}
