package com.renan.booksalesonline.adapters.repositories.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renan.booksalesonline.domain.Country;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class RedisCacheImplTest {

    private ValueOperations valueOperations;
    private RedisTemplate<String, String> redisTemplate;
    private RedisCacheImpl redisCache;

    @BeforeAll
    public void init() {

        valueOperations = mock(ValueOperations.class);
        redisTemplate = mock(RedisTemplate.class);
        redisCache = new RedisCacheImpl(redisTemplate);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void should_get_by_id_any_entity_from_cache() throws JsonProcessingException {

        var entityJson = "{ \"id\": 1, \"name\": \"name\", \"gentilic\": \"gentilic\" }";
        when(redisTemplate.opsForValue().get(any())).thenReturn(entityJson);

        var expectedEntity = new Country(1, "name", "gentilic");
        var actualEntity = redisCache.getById(Country.class, 1);

        assertThat(actualEntity)
                .usingRecursiveComparison()
                .isEqualTo(expectedEntity);
    }

    @Test
    public void should_get_by_id_any_entity_but_not_from_cache() throws JsonProcessingException {

        when(redisTemplate.opsForValue().get(any())).thenReturn(null);

        var actualEntity = redisCache.getById(Country.class, 1);

        assertThat(actualEntity).isNull();
    }

    @Test
    public void should_save_any_entity_in_cache() throws JsonProcessingException {

        doNothing().when(valueOperations).set(anyString(), anyString());
        when(redisTemplate.expire(anyString(), anyLong(), any())).thenReturn(true);

        assertDoesNotThrow(() -> redisCache.save(Country.class, 1));
    }
}
