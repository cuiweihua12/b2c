package com.cwh.controller;

import com.cwh.common.enums.ExceptionEnums;
import com.cwh.common.exception.MyException;
import com.cwh.entity.Person;
import com.cwh.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-07-13 19:18
 */
@RestController
public class ItemController {
    @Autowired
    private ItemService service;
    @GetMapping(value = "/item",produces = "application/json;charset=UTF-8")
    public Map getMap(){

        HashMap<String, String> map = new HashMap<>();
        map.put("a","a");

        return map;
    }

    @PostMapping("/save")
    public ResponseEntity saveItem(@RequestBody Person person){
        if (person.getPrice() == null){
            throw new MyException(ExceptionEnums.PRICE_NOTNULL);
        }
        return ResponseEntity.status(HttpStatus.OK).body(service.saveItem(person));
    }
}
