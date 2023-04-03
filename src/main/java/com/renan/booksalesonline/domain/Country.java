package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.commom.BaseDomain;

public class Country extends BaseDomain {

    private String nationality;

    public Country(int id, String name, String nationality) {
        super(id, name);
        setNationality(nationality);
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
