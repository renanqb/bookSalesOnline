package com.renan.booksalesonline.application.ports.in;

public interface RemoveEntityUseCase {

    <T> void execute(Class<T> clazz, int id) throws NoSuchMethodException;
}
