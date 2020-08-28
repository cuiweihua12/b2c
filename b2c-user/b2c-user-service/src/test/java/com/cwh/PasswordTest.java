package com.cwh;

import com.cwh.utils.PasswordUtil;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.UUID;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-25 22:23
 */

public class PasswordTest {

    @SneakyThrows
    @Test
    public void test(){

        String salt = UUID.randomUUID().toString();
        String oldPassword = PasswordUtil.encode("cwh", salt.getBytes());

//        System.out.println(PasswordUtil.encode("cuiweihua"));
    }
}
