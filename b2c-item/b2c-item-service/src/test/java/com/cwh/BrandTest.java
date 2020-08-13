package com.cwh;

import com.cwh.service.impl.BrandServiceImpl;
import com.cwh.service.impl.CategoryServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-12 15:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItemApplication.class)
public class BrandTest {
    @Autowired
    private BrandServiceImpl service;

    @Autowired
    private CategoryServiceImpl categoryService;
    @Test
    public void test(){
        ArrayList<Long> list = new ArrayList<>();
        list.add(1115l);
        list.add(1528l);
        list.add(1912l);
        list.add(2032l);
        service.queryByIdList(list).forEach(System.out::println);
    }

    @Test
    public void test2(){
        ArrayList<Long> list = new ArrayList<>();
        list.add(1000l);
        list.add(999l);
        list.add(998l);
        list.add(2l);
        categoryService.queryCategory(list).forEach(System.out::println);
    }
}
