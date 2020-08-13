package com.cwh.dao;

import com.cwh.pojo.GoodsCWH;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author CWH
 */
public interface GoodsCWHRepository extends ElasticsearchRepository<GoodsCWH,Long> {
}
