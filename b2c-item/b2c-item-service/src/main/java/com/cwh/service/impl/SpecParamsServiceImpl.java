package com.cwh.service.impl;

import com.cwh.entity.SpecParam;
import com.cwh.mapper.SpecParamMapper;
import com.cwh.service.SpecParamService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public Integer saveParams(SpecParam specParam) {
        if (specParam.getId() != null){
            return specParamMapper.updateByPrimaryKey(specParam);
        }
        int insertSelective = specParamMapper.insertSelective(specParam);
        rabbitTemplate.convertAndSend("b2c","item.params.save",specParam);
        return insertSelective;
    }

    @Override
    @Transactional
    public Integer deleteParams(Long id) {
        int delete = specParamMapper.deleteByPrimaryKey(id);
        rabbitTemplate.convertAndSend("b2c","item.params.remove",id);
        return delete;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecParam> getParamsByCategoryId(Long categoryId) {
        SpecParam param = new SpecParam();
        param.setCid(categoryId);
        return specParamMapper.select(param);
    }

    @Override
    public List<SpecParam> searchParamsAll() {
        return specParamMapper.selectAll();
    }
}
