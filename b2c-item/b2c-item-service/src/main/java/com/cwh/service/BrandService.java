package com.cwh.service;

import com.cwh.common.result.CommonResult;
import com.cwh.common.utils.PageResult;
import com.cwh.entity.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {
    Brand saveBrand(Brand brand);

    CommonResult deleteBrand(Long id);

    PageResult<Brand> list(Brand brand, Integer page, Integer pageNum, String sortBy, Boolean sortDesc);

    List<Brand> criteraQuery(Brand brand);

    Map<String, Object> queryById(Brand brand);

    List<Brand> searchBrandAll();

    List<Brand> queryBrandByCategoryId(Long cateGoryId);

    Brand queryByBrandId(Long bid);
}
