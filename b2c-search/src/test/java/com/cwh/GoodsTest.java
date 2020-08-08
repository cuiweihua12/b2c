package com.cwh;

import com.cwh.client.ConnectionBrandClient;
import com.cwh.client.ConnectionGoodsClient;
import com.cwh.service.impl.GoodsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-06 14:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsTest {

    @Autowired
    private ConnectionGoodsClient connectionGoodsClient;

    @Autowired
    private ConnectionBrandClient brandClient;
    @Test
    public void goodsList(){
        connectionGoodsClient
                .list("id", true, 0, 10000, "", 1)
                .getBody()
                .getItems()
                .forEach(System.out::println);
    }

    @Autowired
    GoodsServiceImpl goodsService;
    @Test
    public  void Test(){
        goodsService.getGoods();
    }

    @Test
    public void test1(){
//        System.out.println(brandClient.queryByBrandId(235L).getBody());
        goodsService.getGoods2();
    }

    @Test
    public void test3(){
        Integer count = 0;
        while (true){
            count++;
            System.out.println(count);
            if (count == 10){break;}
        }
        System.out.println("循环结束");
    }
}
