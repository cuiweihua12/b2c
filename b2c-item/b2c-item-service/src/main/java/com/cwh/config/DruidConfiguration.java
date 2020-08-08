package com.cwh.config;

import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: b2c
 * @description: 德鲁伊配置
 * @author: cuiweihua
 * @create: 2020-07-19 10:16
 */
@Configuration
public class DruidConfiguration {
    /**
     *@Description: 配置德鲁伊监控  配置一个管理后台的servlet
     *@Param: []
     *@return: org.springframework.boot.web.servlet.ServletRegistrationBean
     *@Author: cuiweihua
     *@date: 2020/7/5
     */
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String,String> initParams = new HashMap<>();
        //设置登录账户
        initParams.put("loginUsername","admin");
        //设置登录密码
        initParams.put("loginPassword","admin");
        //默认就是允许所有访问
        initParams.put("allow","");
        //黑名单
        initParams.put("deny","192.168.15.21");
        bean.setInitParameters(initParams);
        return bean;
    }
}
