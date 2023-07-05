package com.pro.cloud.cache.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author lenovo
 * @Date 2023/6/30
 * @Des
 */
@Configuration
public class RedissonConfig {

    @Autowired
    private RedisClusterNodesProperties clusterNodesProperties;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        ClusterServersConfig serversConfig = config.useClusterServers();
        serversConfig.setPassword(password);
        for (String node : clusterNodesProperties.getNodes()) {
            serversConfig.addNodeAddress("redis://" + node);
        }
        return Redisson.create(config);
    }

}
