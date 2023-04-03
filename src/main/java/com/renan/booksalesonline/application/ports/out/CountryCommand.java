package com.renan.booksalesonline.application.ports.out;

import com.renan.booksalesonline.domain.Country;

public interface CountryCommand {

    Country save(Country country);

    void remove(Object id);
}
