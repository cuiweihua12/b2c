package com.cwh.pojo;

import com.cwh.bo.BrandBo;
import com.cwh.bo.CategoryBo;
import com.cwh.common.utils.PageResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-12 14:40
 */
@NoArgsConstructor
@ToString
@Data
public class SearchResult<T> extends PageResult<T> {

    private List<CategoryBo> categoryList;

    private List<BrandBo> brandList;

    private List<Map<String,Object>> specMapList;


    public SearchResult(List<CategoryBo> categoryList, List<BrandBo> brandList, List<Map<String, Object>> specMapList) {
        this.categoryList = categoryList;
        this.brandList = brandList;
        this.specMapList = specMapList;
    }

    public SearchResult(Long total, List<T> items, List<CategoryBo> categoryList, List<BrandBo> brandList, List<Map<String, Object>> specMapList) {
        super(total, items);
        this.categoryList = categoryList;
        this.brandList = brandList;
        this.specMapList = specMapList;
    }

    public SearchResult(Long total, Long totalPage, List<T> items, List<CategoryBo> categoryList, List<BrandBo> brandList, List<Map<String, Object>> specMapList) {
        super(total, totalPage, items);
        this.categoryList = categoryList;
        this.brandList = brandList;
        this.specMapList = specMapList;
    }
}
