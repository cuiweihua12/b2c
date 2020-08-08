package com.cwh.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.UnknownHostException;
import java.time.Duration;

/**
 * @program: b2c
 * @description: redis配置
 * @author: cuiweihua
 * @create: 2020-07-19 10:58
 */
//@Configuration
public class RedisConfig {
    //自定义redis模板
    @Bean
    public RedisTemplate<Object, Object> userRedisTemplate(RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        //设置redis默认序列化方式
        template.setDefaultSerializer(serializer);
        return template;
    }
    //自定义缓存管理
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory){
        //配置redis缓存配置
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                //设置过期时间  一分
                .entryTtl(Duration.ofMinutes(3))
                //禁用缓存空值
                .disableCachingNullValues()
                //key序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                //value序列化方式
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        //初始化RedisCacheManager
        return RedisCacheManager.builder(factory).cacheDefaults(cacheConfiguration).build();
    }
}
