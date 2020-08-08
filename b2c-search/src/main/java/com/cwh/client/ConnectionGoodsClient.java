package com.cwh.client;

import com.cwh.api.GoodsClient;
import com.cwh.common.ServiceConstant;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author CWH
 */
@FeignClient(value = ServiceConstant.ITEMSERVICE)
public interface ConnectionGoodsClient extends GoodsClient{

}
