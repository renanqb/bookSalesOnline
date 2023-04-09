package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.commom.BaseDomain;

public class Publisher extends BaseDomain {

    private String history;
    private Country country;

    public Publisher(int id, String name, String history, Country country) {
        super(id, name);
        setHistory(history);
        setCountry(country);
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
