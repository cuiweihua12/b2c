package com.cwh.dao;

import com.cwh.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author CWH
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
