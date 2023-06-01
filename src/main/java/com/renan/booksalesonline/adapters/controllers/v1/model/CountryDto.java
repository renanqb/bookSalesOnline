package com.renan.booksalesonline.adapters.controllers.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.renan.booksalesonline.adapters.controllers.v1.commom.BaseDto;

public class CountryDto extends BaseDto {

    @JsonProperty("gentilic") private String gentilic;

     CountryDto() { super(""); }

    public CountryDto(String name, String gentilic) {
        super(name);
        this.gentilic = gentilic;
    }

    public String getGentilic() {
        return gentilic;
    }
}
