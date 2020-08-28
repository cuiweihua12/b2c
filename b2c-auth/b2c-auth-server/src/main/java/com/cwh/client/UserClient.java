package com.cwh.client;

import com.cwh.api.UserApi;
import com.cwh.common.constant.ServiceConstant;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-26 21:32
 */
@FeignClient(value = ServiceConstant.USERSERVER)
public interface UserClient extends UserApi {

}
