package com.pro.cloud.cache.config;

import com.alibaba.fastjson.parser.ParserConfig;
import com.pro.cloud.cache.cachemanage.TtlRedisCacheManage;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @Author lenovo
 * @Date 2023/6/29
 * @Des redis配置类
 */
@Configuration
public class RedisManagerConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        template.setKeySerializer(redisSerializer);
        template.setHashKeySerializer(redisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    /**
     * 这个方法使用了RedisConnectionFactory来创建一个非锁定的RedisCacheWriter，该RedisCacheWriter用于执行缓存操作而不使用锁来保证线程安全性。然后，它使用StringRedisSerializer和GenericJackson2JsonRedisSerializer来对缓存的键和值进行序列化。默认的缓存配置使用了这些序列化器，并设置了缓存的过期时间为100秒。
     * 最后，这个方法创建了一个TtlRedisCacheManager实例，该实例使用前面配置的RedisCacheWriter和缓存配置来管理Redis缓存。TtlRedisCacheManager是Spring Data Redis库提供的一个特殊的缓存管理器，它可以为每个缓存条目设置过期时间。
     * 总的来说，这个方法的目的是创建一个Redis缓存管理器，并配置了一些缓存的序列化和过期时间等属性。
     *
     * 使用与注解场景
     * @param connectionFactory
     * @return
     */
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        //String类型的序列化器
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        //非锁定的RedisCacheWriter，在进行缓存操作时不会使用锁来保证线程安全性，如果对缓存一致性要求高可以使用lockingRedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        //用于序列化缓存值
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                //缓存过期时间100秒
                .entryTtl(Duration.ofSeconds(100L));
        RedisCacheManager cacheManager = new TtlRedisCacheManage(redisCacheWriter, defaultCacheConfig);
        //全局解析配置 自动识别对象的类型并正确地进行反序列化操作
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        return cacheManager;
    }

}
