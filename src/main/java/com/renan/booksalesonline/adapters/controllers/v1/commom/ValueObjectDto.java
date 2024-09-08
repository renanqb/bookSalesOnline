package com.renan.booksalesonline.adapters.controllers.v1.commom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ValueObjectDto extends BaseDto {

    public ValueObjectDto(Object id, String name) {
        super(name);
        this.id = id;
    }
}
