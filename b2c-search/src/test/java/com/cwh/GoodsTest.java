package com.cwh;

import com.cwh.bo.CategoryBo;
import com.cwh.client.ConnectionBrandClient;
import com.cwh.client.ConnectionGoodsClient;
import com.cwh.client.ConnectionSpecClient;
import com.cwh.service.impl.GoodsCwhServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private ConnectionSpecClient specClient;

    @Autowired
    private GoodsCwhServiceImpl getCwhService;

    @Test
    public void goodsList(){
        connectionGoodsClient
                .list("id", true, 0, 10000, "", 1)
                .getBody()
                .getItems()
                .forEach(System.out::println);
    }

    @Test
    public void saveEs(){
        getCwhService.getGoods(235l,null,null,null);
    }


    @Autowired
    GoodsCwhServiceImpl cwhService;

    @Test
    public  void testBrand(){
        ArrayList<Long> list = new ArrayList<>();
        list.add(76L);
        list.add(75L);
        list.add(74L);
        String[] array = list.stream().map(String::valueOf).toArray(String[]::new);
        List<CategoryBo> brandBos = getCwhService.searchCategoryEs(array);
        brandBos.forEach(System.out::println);
    }

    @Test
    public void testCate(){
        getCwhService.searchParamsEs(76l).stream().forEach(System.out::println);
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

    @Test
    public void test4(){
        specClient.searchSpecParamsBySpecGroupId(null,76l,null).getBody().forEach(System.out::println);
    }
}
