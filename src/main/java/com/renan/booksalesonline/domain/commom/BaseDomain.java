package com.renan.booksalesonline.domain.commom;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public abstract class BaseDomain {

    private int id;

    @NotBlank
    private String name;

    public BaseDomain() {
        this(0, "");
    }

    public BaseDomain(int id, String name) {
        setId(id);
        setName(name);
    }
}
