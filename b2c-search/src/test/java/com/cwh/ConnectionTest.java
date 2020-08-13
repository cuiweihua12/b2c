package com.cwh;

import com.cwh.api.BrandClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-12 11:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConnectionTest {

    @Resource
    BrandClient brandClient;
    @Test
    public void  test(){
        brandClient.getBrandAll().getBody().forEach(System.out::println);
    }

}
