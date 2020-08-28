package com.cwh.filter;

import com.cwh.common.utils.CookieUtils;
import com.cwh.config.FilterProperties;
import com.cwh.config.JwtConfig;
import com.cwh.utils.JwtUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: b2c
 * @description:
 * @author: cuiweihua
 * @create: 2020-08-27 19:29
 */
@Component
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private FilterProperties filterProperties;

    private final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    /**
     * 过滤器类型
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 过滤优先级
     * @return
     */
    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER;
    }

    /**
     * 拦截需要过滤的请求
     * @return
     */
    @Override
    public boolean shouldFilter() {
        RequestContext context = new RequestContext();
        HttpServletRequest request = context.getRequest();
        return !isAllowIgnore(request.getRequestURI());
    }

    private Boolean isAllowIgnore(String uri){
        boolean flage = false;
        for (String f : filterProperties.getAllowsignore()) {
            if (uri.startsWith(f)) {
                flage = true;
                break;
            }
        }
        return flage;
    }


    @Override
    public Object run() throws ZuulException {
        //获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        logger.info("拦截请求："+request.getRequestURI());
        //获取token
        String token = CookieUtils.getCookieValue(request, jwtConfig.getCookieName());
        logger.info("token："+token);
        if (StringUtils.isEmpty(token)){
            //拦截
            context.setSendZuulResponse(false);
            //返回状态码
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
        //解析token
        try {
            JwtUtils.getInfoFromToken(token,jwtConfig.getPublicKey());
        } catch (Exception e) {
            logger.info("token解析失败！");
            //拦截
            context.setSendZuulResponse(false);
            //返回状态码
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}
