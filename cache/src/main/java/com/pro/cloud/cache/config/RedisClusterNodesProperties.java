package com.pro.cloud.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lenovo
 * @Date 2023/6/30
 * @Des
 */
@Component
@ConfigurationProperties(prefix = "spring.redis.cluster")
@Data
public class RedisClusterNodesProperties {

    private List<String> nodes = new ArrayList<>();

}
