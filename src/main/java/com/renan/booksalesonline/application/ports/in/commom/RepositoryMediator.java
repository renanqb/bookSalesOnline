package com.renan.booksalesonline.application.ports.in.commom;

import com.renan.booksalesonline.application.ports.out.DataCommand;
import com.renan.booksalesonline.application.ports.out.DataQuery;

public interface RepositoryMediator {

    <T> DataQuery<T> getQuery(Class<T> clazz) throws NoSuchMethodException;

    <T> DataCommand<T> getCommand(Class<T> clazz) throws NoSuchMethodException;
}
