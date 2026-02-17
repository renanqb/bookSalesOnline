package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.commom.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Subject extends BaseDomain {

    @NotBlank
    private String description;

    public Subject(int id) {
        this(id, "", "");
    }

    public Subject(int id, String name, String description) {
        super(id, name);
        setDescription(description);
    }
}

