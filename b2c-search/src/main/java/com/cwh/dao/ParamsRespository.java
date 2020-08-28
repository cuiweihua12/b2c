package com.cwh.dao;

import com.cwh.bo.SpecParamBo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ParamsRespository extends ElasticsearchRepository<SpecParamBo,Long> {
}
