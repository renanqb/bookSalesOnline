package com.renan.booksalesonline.application.ports.out;

import com.renan.booksalesonline.application.ports.out.base.DataCommandQuery;

import java.util.List;

public interface DataQuery<T> extends DataCommandQuery {

    List<T> getAll(int page, int size);

    T getById(int id);
}
