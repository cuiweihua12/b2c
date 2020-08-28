package com.cwh;

import org.junit.Test;

import java.util.ArrayList;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-27 21:19
 */
public class ListTest {
    @Test
    public void test(){
        ArrayList<String> list = new ArrayList<>();
        list.add("/api/search/goods/page");
        list.add("/api/auth/verify");
        list.add("/api/auth/check");
        list.add("/api/user/check");

        System.out.println(list.contains("/api/search/goods/page"));
        System.out.println(list.contains("/api/search/a/page"));

    }
}
