package com.cwh;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-12 08:43
 */
public class QuitTest {

    @Test
    public void test(){
        String aaa = new String("aaa");
        String bbb = new String("aaa");
        Boolean a = new Boolean("a");
        Character a1 = new Character('A');
        System.out.println(aaa.equals(bbb));
        System.out.println(aaa == bbb);
        System.out.println(a);
        System.out.println(Character.isLowerCase(a1));
    }
    @Test
    public void test2(){
        int arr[][] = new int[233][666];
        int arr1[][] = new int[1][];
        int arr2[][] = {{1,2},{5,1,8}};

        int [ ][ ]  arrs={{22,15,32,20,18},{12,21,25,19,33},{14,58,34,24,66}};
        System.out.println(arrs[2][0]);
    }

    @Test
    public  void  test3(){
        ArrayList<Long> list = new ArrayList<>();
        list.add(1115l);
        list.add(1528l);
        list.add(1912l);
        String[] array = list.stream().map(String::valueOf).toArray(String[]::new);
        System.out.println(Arrays.stream(array).toString());
        System.out.println(array.length);
    }
}
