package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.commom.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Publisher extends BaseDomain<Integer> {

    private String history;
    private Country country;

    public Publisher() {

        this(0, "", "", null);
    }

    public Publisher(int id, String name, String history, Country country) {

        super(id, name);
        setHistory(history);
        setCountry(country);
    }
}
