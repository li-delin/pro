package com.pro.cloud.cache.cachemanage;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * @Author lenovo
 * @Date 2023/6/29
 * @Des 这个类是一个自定义的Redis缓存管理器类，继承自RedisCacheManager。在Spring Boot中，可以通过自定义RedisCacheManager来对Redis缓存进行更灵活的配置和管理。
 * 在这个类中，重写了父类的createRedisCache方法。createRedisCache方法用于创建一个RedisCache实例，用于管理具体的缓存。
 * 在重写的createRedisCache方法中，首先使用StringUtils的delimitedListToStringArray方法将缓存名称按照"#"进行分割，获取缓存名称和过期时间。
 * 然后，判断是否存在过期时间，如果存在，则将缓存配置的过期时间设置为指定的过期时间。
 * 最后，调用父类的createRedisCache方法创建一个RedisCache实例，并返回。
 * 通过自定义RedisCacheManager和重写createRedisCache方法，可以实现对Redis缓存的更灵活的配置，例如设置不同缓存的不同过期时间，或者对缓存名称进行处理等。
 */
public class TtlRedisCacheManage extends RedisCacheManager {

    public TtlRedisCacheManage(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        String[] array = StringUtils.delimitedListToStringArray(name, "#");
        name = array[0];
        if (array.length > 1) {
            long ttl = Long.parseLong(array[1]);
            cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(ttl));
        }
        return super.createRedisCache(name, cacheConfig);
    }
}
