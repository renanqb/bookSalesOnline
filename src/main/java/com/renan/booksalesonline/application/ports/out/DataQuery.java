package com.renan.booksalesonline.application.ports.out;

import com.renan.booksalesonline.application.ports.out.base.DataCommandQuery;

import java.util.List;

public interface DataQuery<T> extends DataCommandQuery {

    List<T> getAll();

    T getById(int id);
}
