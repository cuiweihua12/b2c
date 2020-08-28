package com.cwh.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-27 21:14
 */
@Configuration
@ConfigurationProperties(prefix = "b2c.filter")
@Data
public class FilterProperties {

    private List<String> allowsignore;
}
