package com.cwh.service.impl;

import com.cwh.client.UserClient;
import com.cwh.config.JwtConfig;
import com.cwh.jopo.UserInfo;
import com.cwh.pojo.User;
import com.cwh.service.AuthService;
import com.cwh.utils.JwtUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-26 21:31
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtConfig jwtConfig;

    @SneakyThrows
    @Override
    public String login(String username, String password) {

        User user = userClient.queryByUsernameOrPassword(username, password);
        if (!ObjectUtils.isEmpty(user)){
            String token = null;
            return token = JwtUtils.generateToken(new UserInfo(user.getId(), username), jwtConfig.getPrivateKey(), jwtConfig.getCookieMaxAge());
        }
        return null;
    }
}
