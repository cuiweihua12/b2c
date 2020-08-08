package com.cwh;

import com.cwh.bo.SkuBo;
import com.cwh.entity.Sku;
import com.cwh.entity.Spu;
import com.cwh.entity.SpuDetail;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-07-28 20:43
 */

public class mapTest {

    @Test
    public void teset3(){
        Object a = 5;
        Long b = Long.valueOf(String.valueOf(a)).longValue();
    }

    @Test
    public void test2(){
        Spu spu = new Spu();
        Class<? extends Spu> aClass = spu.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            String type = declaredField.getType().getName();
            System.out.println(type);
        }
    }
    @Test
    public  void test() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Long a = 1L;
        map.put("a",a);
        map.put("a",new SpuDetail());
        map.forEach((k,v)->{
            String type = map.get(k).getClass().toString();
            System.out.println(type);
            type = type.substring(type.lastIndexOf('.') + 1, type.length());
            if("Long".equals(type)) {
                System.out.println(map.get(k));
            }

        });
    }

    public static Boolean getType(Object o) {
        String type = o.getClass().toString();
        System.out.println(type);
        type = type.substring(type.lastIndexOf('.') + 1, type.length());
        if("Long".equals(type)) {
            return true;
        }
        ArrayList<Object> objects = new ArrayList<>();
        return false;
    }
    @Test
    public  void test33(){
        List<SkuBo> list = new ArrayList<>();
        SkuBo skuBo = new SkuBo();
        skuBo.setCreateTime(new Date());
        list.add(skuBo);
        Sku sku = new Sku();
        for (SkuBo skuBos : list) {
            BeanUtils.copyProperties(skuBos,sku);
            System.out.println(sku.toString());
        }
//        BeanUtils.copyProperties(list,sku);
//        System.out.println(sku.toString());
    }
}
