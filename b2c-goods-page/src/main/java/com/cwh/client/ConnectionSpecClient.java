package com.cwh.client;

import com.cwh.api.SpecClient;
import com.cwh.common.constant.ServiceConstant;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-06 15:40
 */
@FeignClient(value = ServiceConstant.ITEMSERVICE)
public interface ConnectionSpecClient extends SpecClient {
}
