package com.cwh.service;

import com.cwh.entity.SpecParam;

import java.util.List;

public interface SpecParamService {
    Integer saveParams(SpecParam specParam);

    Integer deleteParams(Long id);

    List<SpecParam> getParamsByCategoryId(Long categoryId);
}
