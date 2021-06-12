package com.shanjupay.transaction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.shanjupay.common.cache.Cache;
import com.shanjupay.transaction.common.util.RedisCache;
/**
 * 描述
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/29 23:55
 **/
@Configuration
public class RedisConfig {
    @Bean
    public Cache cache(StringRedisTemplate redisTemplate) {
        return new RedisCache(redisTemplate);
    }
}
