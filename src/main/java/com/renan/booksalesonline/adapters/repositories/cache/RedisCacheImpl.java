package com.renan.booksalesonline.adapters.repositories.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.renan.booksalesonline.application.ports.out.base.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheImpl implements RedisCache {

    private final ObjectMapper mapper = new ObjectMapper();
    private final RedisTemplate<String, String> redisTemplate;
    @Value("${spring.redis.expires}") private Integer redisExpires;

    public RedisCacheImpl(
            @Autowired @Qualifier("customRedisTemplate") RedisTemplate<String, String> redisTemplate) {

        this.redisTemplate = redisTemplate;
        this.redisExpires = Objects.requireNonNullElse(redisExpires, 1);
    }

    @Override
    public <T> T getById(Class<T> clazz, int id) throws JsonProcessingException {

        var key = String.format("%s/%s", clazz.getSimpleName(), id);
        var jsonValue = redisTemplate.opsForValue().get(key);
        if (jsonValue == null) return null;

        return mapper.readValue(jsonValue, clazz);
    }

    @Override
    public <T> void save(T domain, int id) throws JsonProcessingException {

        var key = String.format("%s/%s", domain.getClass().getSimpleName(), id);
        redisTemplate.opsForValue().set(key, mapper.writeValueAsString(domain));
        redisTemplate.expire(key, redisExpires, TimeUnit.MINUTES);
    }
}
