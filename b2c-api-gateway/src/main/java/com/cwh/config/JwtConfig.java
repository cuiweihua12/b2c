package com.cwh.config;

import com.cwh.utils.RsaUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @program: b2c
 * @description: jwt配置类
 * @author: cuiweihua
 * @create: 2020-08-27 19:42
 */
@Configuration
@ConfigurationProperties(prefix = "b2c.jwt")
@Data
public class JwtConfig {

    private Integer secret;

    private String pubKeyPath;

    private PublicKey publicKey;

    private String cookieName;

    private final Logger logger = LoggerFactory.getLogger(JwtConfig.class);

    @PostConstruct
    public void init(){
        try {
            //获取公钥
            publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(" 公钥初始化失败！");
        }
    }

}
