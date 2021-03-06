package com.cwh.config;

import com.cwh.utils.RsaUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@Configuration
@PropertySource("classpath:application.yml")
public class JwtConfig {
    @Value("${b2c.jwt.secret}")
    /**
     *  密钥
     */
    private String secret;
    @Value("${b2c.jwt.pubKeyPath}")
    /**
     * 公钥
     */
    private String pubKeyPath;
    @Value("${b2c.jwt.priKeyPath}")
    /**
     * 私钥
     */
    private String priKeyPath;
    @Value("${b2c.jwt.expire}")
    /**
     * token过期时间
     */
    private int expire;
    @Value("${b2c.jwt.cookieName}")
    /**
     * cookie名称
     */
    private String cookieName;
    @Value("${b2c.jwt.cookieMaxAge}")
    /**
     * cookie名称
     */
    private int cookieMaxAge;

    /**
     * 公钥
     */
    private PublicKey publicKey;

    /**
     * 私钥
     */
    private PrivateKey privateKey;

    private static final Logger logger = LoggerFactory.getLogger(JwtConfig.class);

    @PostConstruct
    public void init(){
        try {
            File pubKey = new File(pubKeyPath);
            File priKey = new File(priKeyPath);
            if (!pubKey.exists() || !priKey.exists()) {
                // 生成公钥和私钥
                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);
            }
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
        } catch (Exception e) {
            logger.error("初始化公钥和私钥失败！", e);
            throw new RuntimeException();
        }
    }

}
