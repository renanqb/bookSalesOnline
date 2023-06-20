package com.renan.booksalesonline.adapters.controllers.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.renan.booksalesonline.adapters.controllers.v1.commom.BaseDto;

public class SubjectDto extends BaseDto {

    @JsonProperty("description") private String description;

    SubjectDto() { super(""); }

    public SubjectDto(String name, String description) {
        super(name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
