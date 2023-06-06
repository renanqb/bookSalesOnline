package com.renan.booksalesonline.application.ports.out.base;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface RedisCache {

    <T> T getById(Class<T> clazz, int id) throws JsonProcessingException;

    <T> void save(T domain, int id) throws JsonProcessingException;
}
