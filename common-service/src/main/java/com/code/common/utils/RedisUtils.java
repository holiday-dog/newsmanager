package com.code.common.utils;

import org.springframework.data.redis.core.RedisTemplate;

public class RedisUtils {
    private static RedisTemplate<String, String> redisTemplate = null;

    private static RedisTemplate<String, String> getRedisTemplate() {
        if (redisTemplate == null) {
            synchronized (RedisTemplate.class) {
                if (redisTemplate == null) {
                    redisTemplate = RedisFactory.buildStringRedisTemplate();
                }
            }
        }
        return redisTemplate;
    }

    public static String getValueByKey(String key) {
        return getRedisTemplate().opsForValue().get(key);
    }


}
