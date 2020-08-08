package com.cwh.service.impl;

import com.cwh.entity.SpecGroup;
import com.cwh.entity.SpecList;
import com.cwh.entity.SpecParam;
import com.cwh.mapper.SpecGroupMapper;
import com.cwh.mapper.SpecParamMapper;
import com.cwh.service.SpecGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-07-23 13:59
 */
@Service
public class SpecGroupServiceImpl implements SpecGroupService {

    @Resource
    private SpecGroupMapper specGroupMapper;

    @Resource
    private SpecParamMapper specParamMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SpecGroup> searchSpecGroupByCategoruId(Long categoryId) {
        SpecGroup group = new SpecGroup();
        group.setCid(categoryId);
        return specGroupMapper.select(group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecParam> searchSpecParams(Long groupId, Long categoryId) {
        SpecParam param = new SpecParam();
        param.setGroupId(groupId);
        param.setCid(categoryId);
        return specParamMapper.select(param);
    }

    @Override
    @Transactional
    public Integer saveGroup(SpecGroup specGroup) {
        if (specGroup.getId() != null){
            return specGroupMapper.updateByPrimaryKeySelective(specGroup);
        }
        return specGroupMapper.insertSelective(specGroup);
    }

    @Override
    @Transactional
    public Integer deleteGroup(Long id) {
        return specGroupMapper.deleteByPrimaryKey(id);
    }

    @Override
    public SpecList getSearchGroupParmas(Long categoryId) {
        //查询所有参数组
        List<SpecGroup> specGroups = searchSpecGroupByCategoruId(categoryId);
        SpecParam specParam = null;
        //查询参数组拥有参数
        for (SpecGroup group : specGroups) {
            specParam = new SpecParam();
            specParam.setGroupId(group.getId());
            List<SpecParam> params = specParamMapper.select(specParam);
            group.setParams(params);
        }
        SpecList specList = new SpecList();
        specList.setGroup(specGroups);
        return specList;
    }
}
