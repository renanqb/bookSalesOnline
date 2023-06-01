package com.renan.booksalesonline.adapters.controllers.v1.commom;

public class ValueObjectDto extends BaseDto {

    public ValueObjectDto() {
        this(0, "");
    }

    public ValueObjectDto(Object id, String name) {
        super(name);
        this.id = id;
    }
}
