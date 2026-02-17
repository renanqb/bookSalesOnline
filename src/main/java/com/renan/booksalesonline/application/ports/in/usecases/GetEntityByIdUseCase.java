package com.renan.booksalesonline.application.ports.in.usecases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renan.booksalesonline.domain.exceptions.NotFoundException;

public interface GetEntityByIdUseCase {

    <T> T execute(Class<T> clazz, int id) throws NotFoundException, NoSuchMethodException, JsonProcessingException;
}
