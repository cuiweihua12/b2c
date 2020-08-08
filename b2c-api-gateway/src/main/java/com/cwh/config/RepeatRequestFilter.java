package com.cwh.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @program: b2c
 * @description: 防止重复发送请求
 * @author: cuiweihua
 * @create: 2020-07-19 11:59
 */
@Component
public class RepeatRequestFilter extends ZuulFilter {

    private Log logger = LogFactory.getLog(getClass());
    @Resource
    private RedisTemplate<String,Integer> redisTemplate;
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return -5;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        ValueOperations<String, Integer> opsForValue = redisTemplate.opsForValue();
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            //获取请求sessionid
            String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
            //获取请求
            HttpServletRequest request = attributes.getRequest();
            //请求路径
            String key = sessionId + "-" + request.getServletPath();
            RequestContext context = RequestContext.getCurrentContext();
            // 如果缓存中有这个url视为重复提交
            if (opsForValue.get(key) == null) {
                opsForValue.set(key, 0, 2, TimeUnit.SECONDS);

            } else {
                logger.error("重复提交");
                //拦截
                context.setSendZuulResponse(false);
                context.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
            }
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error("验证重复提交时出现未知异常!");
            return "{\"code\":-889,\"message\":\"验证重复提交时出现未知异常!\"}";
        }
        return null;
    }
}
