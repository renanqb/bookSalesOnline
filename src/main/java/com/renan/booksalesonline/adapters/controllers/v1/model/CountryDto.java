package com.renan.booksalesonline.adapters.controllers.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.renan.booksalesonline.adapters.controllers.v1.commom.BaseDto;

public class CountryDto extends BaseDto {

    @JsonProperty("gentilic") private String nationality;

    public CountryDto(String name, String nationality) {
        super(name);
        this.nationality = nationality;
    }

    public String getNationality() {
        return nationality;
    }
}
