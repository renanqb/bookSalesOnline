package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.commom.BaseDomain;

public class Country extends BaseDomain {

    private String gentilic;

    public Country(int id, String name, String gentilic) {
        super(id, name);
        setNationality(gentilic);
    }

    public String getNationality() {
        return gentilic;
    }

    public void setNationality(String nationality) {
        this.gentilic = nationality;
    }
}
