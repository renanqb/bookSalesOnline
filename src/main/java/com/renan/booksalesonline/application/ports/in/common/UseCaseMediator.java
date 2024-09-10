package com.renan.booksalesonline.application.ports.in.common;

public interface UseCaseMediator {

    <T> T get(Class<T> clazz) throws NoSuchMethodException;
}
