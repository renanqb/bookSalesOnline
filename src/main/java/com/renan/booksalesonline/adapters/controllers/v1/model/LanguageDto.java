package com.renan.booksalesonline.adapters.controllers.v1.model;

import com.renan.booksalesonline.adapters.controllers.v1.commom.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LanguageDto extends BaseDto {

    public LanguageDto(String name) {
        super(name);
    }
}
