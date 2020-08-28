package com.cwh.dao;

import com.cwh.bo.CategoryBo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-13 15:24
 */
public interface CategoryRespostory extends ElasticsearchRepository<CategoryBo,Long> {
}
