package com.renan.booksalesonline.application.ports.in.usecases;

import com.renan.booksalesonline.domain.commom.BaseDomain;
import org.jetbrains.annotations.NotNull;

public interface CreateEntityUseCase {

    <T> T execute(Class<T> clazz, @NotNull BaseDomain domain) throws NoSuchMethodException;
}
