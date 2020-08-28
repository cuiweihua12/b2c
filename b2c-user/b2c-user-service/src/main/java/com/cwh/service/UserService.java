package com.cwh.service;

import com.cwh.pojo.User;

public interface UserService {
    Boolean check(String data, Integer type);

    void register(String username, String password, String phone);

    User queryByUsernameOrPassword(String username, String password);
}
