package com.renan.booksalesonline.adapters.controllers.v1.mappers;

import com.renan.booksalesonline.adapters.controllers.v1.model.CountryResponse;
import com.renan.booksalesonline.domain.Country;

import java.util.List;
import java.util.stream.Collectors;

public class CountryResponseMapper {

    public static CountryResponse fromDomain(Country country) {

        return new CountryResponse(
                country.getId(),
                country.getName(),
                country.getNationality()
        );
    }

    public static List<CountryResponse> fromDomain(List<Country> countries) {

        return countries.stream()
                .map(CountryResponseMapper::fromDomain)
                .collect(Collectors.toList());
    }
}
