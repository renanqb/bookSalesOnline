package com.renan.booksalesonline.adapters.controllers.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.renan.booksalesonline.adapters.controllers.v1.commom.BaseDto;
import com.renan.booksalesonline.adapters.controllers.v1.commom.ValueObjectDto;

public class PublisherDto extends BaseDto {

    @JsonProperty("history") private String history;
    @JsonProperty("country") private ValueObjectDto country;

    public PublisherDto() {
        this("", "", null);
    }

    public PublisherDto(String name, String history, CountryDto country) {
        super(name);
        this.history = history;

        if (country != null)
            this.country = new ValueObjectDto(
                    country.getId(),
                    country.getName()
            );
    }

    public String getHistory() {
        return history;
    }

    public ValueObjectDto getCountry() {
        return country;
    }
}
