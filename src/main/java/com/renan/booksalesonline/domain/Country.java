package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.commom.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Country extends BaseDomain {

    private String nationality;

    public Country(int id) {

        this(id, "", "");
    }

    public Country(int id, String name, String nationality) {

        super(id, name);
        setNationality(nationality);
    }
}
