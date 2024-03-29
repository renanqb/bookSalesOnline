package com.renan.booksalesonline.application.ports.in.usecases;

import com.renan.booksalesonline.domain.commom.BaseDomain;

public interface CreateEntityUseCase {

    <T> T execute(Class<T> clazz, BaseDomain domain) throws NoSuchMethodException;
}
