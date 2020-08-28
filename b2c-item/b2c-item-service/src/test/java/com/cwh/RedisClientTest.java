package com.cwh;

import com.cwh.api.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-25 10:47
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisClientTest {

    @Autowired
    private Client redisBloomFilter;

    @Test
    public void test(){

        for (int i = 0;i<10000;i++){
            redisBloomFilter.add("cwh:BF",String.valueOf(i));
        }
        int count =0;
        int flage = 0;
        for(int i =10001;i<20000;i++){
            if (redisBloomFilter.exists("cwh:BF",String.valueOf(i))){
                ++count;
            }else {
                ++flage;
            }
        }
        System.out.println("错误个数："+count+",正确个数："+flage);

    }
}
