package com.pro.cloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "resource.filter.config")
@Data
public class ResourceProperties {

    /**
     * 不需要拦截的url
     */
    private List<String>  excludeUrls = new ArrayList<>();

}
