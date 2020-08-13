package com.cwh.service;

import com.cwh.entity.SpecGroup;
import com.cwh.entity.SpecList;
import com.cwh.entity.SpecParam;

import java.util.List;

public interface SpecGroupService {
    List<SpecGroup> searchSpecGroupByCategoruId(Long categoryId);

    List<SpecParam> searchSpecParams(Long groupId, Long categoryId);

    Integer saveGroup(SpecGroup specGroup);

    Integer deleteGroup(Long id);

    SpecList getSearchGroupParmas(Long categoryId);

    List<SpecParam> getSpecParamsByCidList(List<Long> cids);
}
