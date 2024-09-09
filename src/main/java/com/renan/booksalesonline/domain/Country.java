package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.commom.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Country extends BaseDomain<Integer> {

    private String nationality;

    public Country() {

        this(0);
    }

    public Country(int id) {

        this(id, "", "");
    }

    public Country(int id, String name, String nationality) {

        super(id, name);
        setNationality(nationality);
    }
}
