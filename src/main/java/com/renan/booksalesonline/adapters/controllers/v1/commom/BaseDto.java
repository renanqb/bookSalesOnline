package com.renan.booksalesonline.adapters.controllers.v1.commom;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BaseDto {

    @JsonProperty("id") protected Object id;
    @JsonProperty("name") protected String name;

    public BaseDto(String name) {
        this.id = 0;
        this.name = name;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
