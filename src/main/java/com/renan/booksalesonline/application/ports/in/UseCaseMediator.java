package com.renan.booksalesonline.application.ports.in;

import com.renan.booksalesonline.application.mediators.UseCaseType;

public interface UseCaseMediator {

    <T> T get(Class<T> clazz) throws NoSuchMethodException;
}
