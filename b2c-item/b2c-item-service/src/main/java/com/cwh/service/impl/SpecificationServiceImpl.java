package com.cwh.service.impl;

import com.cwh.mapper.SpecificationMapper;
import com.cwh.service.SpecificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: b2c
 * @description: 商品分类规格参数模板业务
 * @author: cuiweihua
 * @create: 2020-07-25 11:18
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Resource
    private SpecificationMapper specificationMapper;

    @Override
    @Transactional(readOnly = true)
    public Map<String,Object> getSpecificationByCategroupId(Long categoryId) {
        HashMap<String, Object> map = new HashMap<>();

        return map;
    }
}
