package com.cwh;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-07-21 08:43
 */
public class Paixu {
    @Test
    public void paixuTest(){
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("a","a");
        objectObjectHashMap.get("a");
        String a = "a";
        String s = new String("a");
        //1 12 312 3
        int i,j,temp;
        Integer [] arr = {12,1,312,3,234,5,326,4,67,78,9,79,7,0,80,};
        for (i = 1; i < arr.length; i++) {
            temp = arr[i];
            for (j = i-1; j >=0; j--) {
                if (temp>arr[j]){
                    break;
                }else {
                    arr[j+1] = arr[j];
                }
            }

            arr[j+1] = temp;
        }
        Arrays.stream(arr).forEach(System.out::println);
    }
}
