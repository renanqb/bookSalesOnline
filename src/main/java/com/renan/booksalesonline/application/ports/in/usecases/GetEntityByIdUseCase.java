package com.renan.booksalesonline.application.ports.in.usecases;

import com.renan.booksalesonline.domain.exceptions.NotFoundException;
import java.util.List;

public interface GetEntityByIdUseCase {

    <T> T execute(Class<T> clazz, int id) throws NotFoundException, NoSuchMethodException;
}
