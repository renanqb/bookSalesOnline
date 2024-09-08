package com.renan.booksalesonline.adapters.controllers.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.renan.booksalesonline.adapters.controllers.v1.commom.BaseDto;
import com.renan.booksalesonline.adapters.controllers.v1.commom.ValueObjectDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true)
public class PublisherDto extends BaseDto {

    @JsonProperty("history")
    private final String history;

    @JsonProperty("country")
    private ValueObjectDto country;

    public PublisherDto(String name, String history, CountryDto country) {
        super(name);
        this.history = history;

        if (country != null)
            this.country = new ValueObjectDto(
                    country.getId(),
                    country.getName()
            );
    }
}
