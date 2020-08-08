package com.cwh.client;

import com.cwh.api.CategoryClient;
import com.cwh.common.ServiceConstant;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author CWH
 */
@FeignClient(value = ServiceConstant.ITEMSERVICE)
public interface ConnectionCategoryClient extends CategoryClient {
}
