package com.renan.booksalesonline.domain.commom;

public abstract class BaseDomain {

    private Object id;
    private String name;

    public BaseDomain() {
        this.id = 0;
        this.name = "";
    }

    public BaseDomain(Object id, String name) {
        this.id = id;
        this.name = name;
    }

    public <T> T getId() {
        return (T)id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
