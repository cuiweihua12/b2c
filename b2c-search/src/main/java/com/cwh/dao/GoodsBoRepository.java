package com.cwh.dao;

import com.cwh.bo.GoodsBo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author CWH
 */
public interface GoodsBoRepository extends ElasticsearchRepository<GoodsBo,Long> {
}
