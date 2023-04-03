package com.renan.booksalesonline.adapters.controllers.v1.commom;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BaseResponse {

    @JsonProperty("id") protected Object id;
    @JsonProperty("name") protected String name;

    public BaseResponse(Object id, String name) {
        this.id = id;
        this.name = name;
    }

    public Object getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
