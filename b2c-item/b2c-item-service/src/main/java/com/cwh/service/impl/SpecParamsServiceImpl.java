package com.cwh.service.impl;

import com.cwh.entity.SpecParam;
import com.cwh.mapper.SpecParamMapper;
import com.cwh.service.SpecParamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: b2c
 * @description: 规格参数业务
 * @author: cuiweihua
 * @create: 2020-07-24 21:03
 */
@Service
public class SpecParamsServiceImpl implements SpecParamService {

    @Resource
    private SpecParamMapper specParamMapper;

    @Override
    @Transactional
    public Integer saveParams(SpecParam specParam) {
        if (specParam.getId() != null){
            return specParamMapper.updateByPrimaryKey(specParam);
        }
        return specParamMapper.insertSelective(specParam);
    }

    @Override
    @Transactional
    public Integer deleteParams(Long id) {
        return specParamMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecParam> getParamsByCategoryId(Long categoryId) {
        SpecParam param = new SpecParam();
        param.setCid(categoryId);
        return specParamMapper.select(param);
    }
}
