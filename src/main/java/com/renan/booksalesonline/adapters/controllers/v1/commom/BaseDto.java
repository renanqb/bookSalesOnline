package com.renan.booksalesonline.adapters.controllers.v1.commom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDto implements Serializable {

    @JsonProperty("id")
    protected Object id = 0;

    @JsonProperty("name")
    protected String name;

    public BaseDto(String name) {
        setId(0);
        setName(name);
    }
}
