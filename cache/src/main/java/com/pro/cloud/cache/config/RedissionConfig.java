package com.pro.cloud.cache.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lenovo
 * @Date 2023/6/30
 * @Des
 */
@Configuration
public class RedissionConfig {

    @Autowired
    private RedisClusterNodesProperties clusterNodesProperties;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useClusterServers()
                .addNodeAddress(clusterNodesProperties.getNodes()
                        .stream()
                        .map(s -> "redis://"+s)
                        .collect(Collectors.joining(",")))
                .setPassword(password);
        return Redisson.create(config);
    }

}
