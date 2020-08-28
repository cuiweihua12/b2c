package com.cwh.service.impl;

import com.cwh.api.Client;
import com.cwh.common.constant.RedisBloomFilter;
import com.cwh.mapper.UserMapper;
import com.cwh.pojo.User;
import com.cwh.service.UserService;
import com.cwh.utils.PasswordUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-25 21:53
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private Client redisBloomFilter;


    public User getUser(){
        return new User();
    }

    public Date getDate(){
        return new Date();
    }

    /**
     * 校验用户是否存在
     * @param data
     * @param type
     * @return
     */
    @Override
    public Boolean check(String data, Integer type) {
        User user = new User();
        switch (type){
            case 1 :user.setUsername(data);break;
            default :user.setPassword(data);
        }
        if (redisBloomFilter.exists(type==1? RedisBloomFilter.USERNAME_FILTER:RedisBloomFilter.PHONE_FILTER,data)) {
            return userMapper.selectCount(user) == 0;
        }
        return true;
    }

    /**
     * 用户注册
     * @param username
     * @param password
     * @param phone
     */
    @SneakyThrows
    @Override
    public void register(String username, String password, String phone) {
        User user = new User();
        user.setCreated(getDate());
        user.setPhone(phone);
        user.setUsername(username);
        user.setSalt(StringUtils.replace(UUID.randomUUID().toString(),"-",""));
        //密码加密加盐
        user.setPassword(PasswordUtil.encode(password));
        userMapper.insert(user);
    }

    /**
     * 根据用户名和密码查询用户信息
     * @param username
     * @param password
     * @return
     */
    @Override
    public User queryByUsernameOrPassword(String username, String password) {
        User condition = getUser();
        condition.setUsername(username);
        User user = userMapper.selectOne(condition);
        if (!ObjectUtils.isEmpty(user) &&  PasswordUtil.comparePassword(password,user.getPassword())){
            return user;
        }
        return null;
    }
}
