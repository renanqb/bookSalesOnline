package com.renan.booksalesonline.application.ports.out;

import com.renan.booksalesonline.application.ports.out.base.DataCommandQuery;

public interface DataCommand<T> extends DataCommandQuery {

    T save(T domain);

    void remove(T domain);
}
