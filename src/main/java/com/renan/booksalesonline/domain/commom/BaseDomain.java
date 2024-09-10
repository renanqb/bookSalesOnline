package com.renan.booksalesonline.domain.commom;

import lombok.Data;

@Data
public abstract class BaseDomain {

    private int id;
    private String name;

    public BaseDomain() {
        this(0, "");
    }

    public BaseDomain(int id, String name) {
        setId(id);
        setName(name);
    }
}
