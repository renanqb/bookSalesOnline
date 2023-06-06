package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.commom.BaseDomain;

public class Country extends BaseDomain {

    private String gentilic;

    public Country() {
        this(0, "", "");
    }

    public Country(int id) {
        this(id, "", "");
    }

    public Country(int id, String name, String gentilic) {
        super(id, name);
        setGentilic(gentilic);
    }

    public String getGentilic() {
        return gentilic;
    }

    public void setGentilic(String gentilic) {
        this.gentilic = gentilic;
    }
}
