package com.renan.booksalesonline.application.ports.out;

import com.renan.booksalesonline.domain.Country;

import java.util.List;

public interface CountryQuery {

    List<Country> getAll();
    Country getById(int id);
}
