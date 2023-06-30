package com.pro.cloud.service;

import com.pro.cloud.cache.config.CacheManagerNames;
import com.pro.cloud.cache.util.RedisTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    private Map<String, Object> DATABASE = new HashMap<String, Object>(){{
        put("k1",12345);
        put("k2",67890);
        put("k3","qazwsx");
        put("k4","edcrfv");
        put("k5","zxcvbn");
    }};

//    @Cacheable(cacheManager = CacheManagerNames.REDIS_CACHE_MANAGER,cacheNames = "GET_USER",sync = true)
    public Object get(String p1,String p2){
        /*redisTemplate.opsForValue().set("p3","12345");
        System.out.println("11");
        System.out.println(redisTemplate.opsForValue().get("p3"));*/
        redisTemplateUtil.set("dl","augdl");
        return redisTemplateUtil.get("dl");
    }


}
