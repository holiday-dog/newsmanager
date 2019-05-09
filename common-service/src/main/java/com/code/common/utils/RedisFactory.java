package com.code.common.utils;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisFactory {
    private static JedisConnectionFactory jedisConnectionFactory = null;
    private static final String host = "localhost";
    private static final Integer port = 6379;
    private static final String password = "";

    public static JedisConnectionFactory buildConnection() {
        if (jedisConnectionFactory == null) {
            synchronized (JedisConnectionFactory.class) {
                if (jedisConnectionFactory == null) {
                    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
                    jedisConnectionFactory = new JedisConnectionFactory(configuration);
                }
            }
        }
        return jedisConnectionFactory;
    }

    public static RedisTemplate buildStringRedisTemplate() {
        RedisTemplate<String, String> redisTemplate = null;

        // 配置redisTemplate
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(buildConnection());
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer); // key序列化
        redisTemplate.setValueSerializer(stringSerializer); // value序列化
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    public static RedisTemplate buildRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = null;

        RedisSerializer jsonRedisSerializer = null;
        jsonRedisSerializer = new GenericFastJsonRedisSerializer();
//        jsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jsonRedisSerializer.setObjectMapper(om);
        // 配置redisTemplate
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(buildConnection());
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer); // key序列化
        redisTemplate.setValueSerializer(jsonRedisSerializer); // value序列化
        redisTemplate.setHashKeySerializer(stringSerializer); // Hash key序列化
        redisTemplate.setHashValueSerializer(jsonRedisSerializer); // Hash value序列化
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
