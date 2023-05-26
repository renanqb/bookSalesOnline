package com.renan.booksalesonline.application.ports.in;

import com.renan.booksalesonline.domain.Country;

import java.util.List;

public interface GetAllEntitiesUseCase {

    <T> List<T> execute(Class<T> clazz) throws NoSuchMethodException;
}
