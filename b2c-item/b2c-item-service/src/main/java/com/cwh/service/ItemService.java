package com.cwh.service;

import com.cwh.entity.Person;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-07-14 11:20
 */
@Service
public class ItemService {


    public Person saveItem(Person person) {
        person.setId(new Random().nextInt(10));
        return person;
    }
}
