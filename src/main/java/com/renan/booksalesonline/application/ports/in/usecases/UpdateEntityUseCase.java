package com.renan.booksalesonline.application.ports.in.usecases;

import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.commom.BaseDomain;
import com.renan.booksalesonline.domain.exceptions.UpdateException;

public interface UpdateEntityUseCase {
    
    <T> T execute(Class<T> clazz, BaseDomain domain, int id) throws NoSuchMethodException, UpdateException;
}
