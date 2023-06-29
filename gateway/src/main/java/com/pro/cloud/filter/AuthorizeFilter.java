package com.pro.cloud.filter;

import com.pro.cloud.config.ResourceProperties;
import io.swagger.annotations.Authorization;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义全局过滤器
 */
@Order(-1)
@Component
public class AuthorizeFilter implements GlobalFilter {

    @Autowired
    private ResourceProperties resourceProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //当前请求路径
        String uri = exchange.getRequest().getURI().toString();
        if (resourceProperties != null){
            for (String excludeUrl : resourceProperties.getExcludeUrls()) {
                if (StringUtils.isEmpty(excludeUrl)){
                    continue;
                }
                Pattern pattern = Pattern.compile(excludeUrl, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(uri);
                if (matcher.find()){
                    return null;
                }
            }
        }
        //获取请求头
        HttpHeaders headers = exchange.getRequest().getHeaders();


        return exchange.getResponse().setComplete();
    }
}
