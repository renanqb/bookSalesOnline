package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.commom.BaseDomain;

public class Subject extends BaseDomain {

    private String description;

    public Subject() {
        this(0, "", "");
    }

    public Subject(Object id, String name, String description) {
        super(id, name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
