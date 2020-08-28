package com.cwh.component;

import com.cwh.api.Client;
import com.cwh.common.constant.RedisBloomFilter;
import com.cwh.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @program: b2c
 * @description: 初始化不隆过滤器
 * @author: cuiweihua
 * @create: 2020-08-26 10:50
 */
@Component
public class InitFilter {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private Client redisBloomFilter;

    private Logger logger = LoggerFactory.getLogger(InitFilter.class);

    /**
     * 项目启动时初始化redis 过滤器
     */
    @PostConstruct
    public void init(){
        userMapper.selectAll().forEach(user -> {
            redisBloomFilter.add(RedisBloomFilter.USERNAME_FILTER,user.getUsername());
            redisBloomFilter.add(RedisBloomFilter.PHONE_FILTER,user.getPhone());
        });
        logger.info("redis 过滤器初始化成功！！");
    }

    /**
     * 项目关闭时删除redis 过滤器中信息
     */
    @PreDestroy
    public void destroy(){
        redisBloomFilter.delete(RedisBloomFilter.USERNAME_FILTER);
        redisBloomFilter.delete(RedisBloomFilter.PHONE_FILTER);
        logger.info("redis 过滤器销毁成功！");
    }

    /**
     * second(秒), minute(分), hour(时), day of month(每月的那天), month(月), and day of week(每周的那天).
     * {@code "0 * * * * MON-FRI"}
     */
    @Scheduled(cron = "* * 0 * * MON-FRI")
    public void clearBloomFilter(){
        try {
            redisBloomFilter.delete(RedisBloomFilter.USERNAME_FILTER);
            redisBloomFilter.delete(RedisBloomFilter.PHONE_FILTER);
            init();
        } catch (Exception e) {
            logger.info("BloomFilter clear success！",e.getMessage());
        }
        logger.info("BloomFilter clear success！");
    }
}
