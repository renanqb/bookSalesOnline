package com.renan.booksalesonline.domain.commom;

import lombok.Data;

@Data
public abstract class BaseDomain<T> {

    private T id;
    private String name;

    public BaseDomain() {
        this(null, "");
    }

    public BaseDomain(T id, String name) {
        setId(id);
        setName(name);
    }
}
