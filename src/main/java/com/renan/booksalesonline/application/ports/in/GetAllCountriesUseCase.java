package com.renan.booksalesonline.application.ports.in;

import com.renan.booksalesonline.domain.Country;

import java.util.List;

public interface GetAllCountriesUseCase {
    List<Country> execute();
}
