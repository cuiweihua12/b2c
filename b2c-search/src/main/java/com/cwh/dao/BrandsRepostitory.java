package com.cwh.dao;

import com.cwh.bo.BrandBo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-13 11:06
 */
public interface BrandsRepostitory extends ElasticsearchRepository<BrandBo,Long> {
}
