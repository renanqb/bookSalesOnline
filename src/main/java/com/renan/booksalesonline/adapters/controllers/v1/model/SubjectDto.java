package com.renan.booksalesonline.adapters.controllers.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.renan.booksalesonline.adapters.controllers.v1.commom.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SubjectDto extends BaseDto {

    @JsonProperty("description")
    private final String description;

    public SubjectDto(String name, String description) {
        super(name);
        this.description = description;
    }
}

