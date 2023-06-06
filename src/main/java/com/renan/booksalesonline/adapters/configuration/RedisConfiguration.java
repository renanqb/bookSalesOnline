package com.renan.booksalesonline.adapters.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfiguration {

    @Value("${spring.redis.host}") private String redisHost;
    @Value("${spring.redis.port}") private Integer redisPort;

    @Bean(name = "customRedisTemplate")
    public RedisTemplate<String, String> redisCache() {

        final var redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new GenericToStringSerializer(String.class));
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());

        var redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        var redisClient = JedisClientConfiguration.builder().build();
        var redisFactory = new JedisConnectionFactory(redisConfig, redisClient);
        redisFactory.afterPropertiesSet();
        redisTemplate.setConnectionFactory(redisFactory);

        return redisTemplate;

    }
}
