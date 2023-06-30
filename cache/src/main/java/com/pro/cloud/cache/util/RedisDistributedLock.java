package com.pro.cloud.cache.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @Author lenovo
 * @Date 2023/6/30
 * @Des 分布式锁
 */
public class RedisDistributedLock {


    private static Logger log = LoggerFactory.getLogger(RedisDistributedLock.class);

    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 无过期时间的加锁
     *
     * @param redisTemplate
     */
    public RedisDistributedLock(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * 无过期时间的加锁方法  true成功 false失败
     *
     * @param lockKey lockKey
     * @param lockValue lockValue
     * @return boolean
     */
    public boolean acquireLock(String lockKey, String lockValue) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue));
    }

    /**
     * 包含过期时间的加锁方法 true成功 false失败
     *
     * @param lockKey   key
     * @param lockValue value
     * @param timeout   timeout
     * @param timeUnit  timeUnit
     * @return boolean
     */
    public boolean acquireLock(String lockKey, String lockValue, long timeout, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, timeout, timeUnit));
    }

    /**
     * 调用redisTemplate方法删除key
     * 不是原子执行
     * @param lockKey
     */
    public void deleteKey(String lockKey) {
        redisTemplate.delete(lockKey);
    }

    /**
     * 使用lua脚本删除key
     * 用于释放redis分布式锁 通过redis.call('get', KEYS[1])命令获取锁的当前值，然后与ARGV[1]进行比较。
     * 如果相等则说明当前线程持有锁，可以通过redis.call('del', KEYS[1])命令删除锁。如果不相等，则说明当前线程没有持有锁，返回0表示释放锁失败。
     * KEYS[1]表示锁的键名，ARGV[1]表示锁的值。通过比较锁的当前值和预期值，可以确保只有持有锁的线程才能释放锁，防止误释放其他线程持有的锁。
     * 保证了原子性
     * @param lockKey
     * @param lockValue
     */
    public void releaseLock(String lockKey, String lockValue) {
        RedisScript<Long> script = new DefaultRedisScript<>("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end", Long.class);
        redisTemplate.execute(script, Collections.singletonList(lockKey), lockValue);
    }


}
