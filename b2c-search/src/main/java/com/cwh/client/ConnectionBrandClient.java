package com.cwh.client;

import com.cwh.api.BrandClient;
import com.cwh.common.ServiceConstant;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author CWH
 */
@FeignClient(value=ServiceConstant.ITEMSERVICE)
public interface ConnectionBrandClient extends BrandClient {
}
