package com.cwh;

import com.cwh.api.GoodsClient;
import com.cwh.entity.Spu;
import com.cwh.service.impl.ItemServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-18 15:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StaticTest {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private GoodsClient goodsClient;
    @Test
    public void test(){

        int page = 0;
        while (true){
            List<Spu> items = goodsClient.list(null, true, page, 10, null, 1)
                    .getBody().getItems();
            items.stream().map(Spu::getId).forEach(i->itemService.createHTMLJuint(i));
            if (items.size()<10){break;}
            ++page;
        }
    }

    @Test
    public void create(){
        itemService.createHTMLJuint(234l);
    }
}
