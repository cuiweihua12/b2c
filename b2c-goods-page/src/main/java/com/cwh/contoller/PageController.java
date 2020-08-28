package com.cwh.contoller;

import com.cwh.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-17 20:01
 */
@Controller
@RequestMapping("item")
public class PageController {

    @Autowired
    private ItemService itemService;

    @GetMapping("{id}.html")
    public String pageHtml(@PathVariable("id") Long id, ModelMap modelMap){
        Map<String,Object> map = itemService.querySpuById(id);
        modelMap.putAll(map);
        return "item";
    }

}
