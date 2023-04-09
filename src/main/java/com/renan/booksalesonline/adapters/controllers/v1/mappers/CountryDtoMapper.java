package com.renan.booksalesonline.adapters.controllers.v1.mappers;

import com.renan.booksalesonline.adapters.controllers.v1.model.CountryDto;
import com.renan.booksalesonline.domain.Country;

import java.util.List;
import java.util.stream.Collectors;

public class CountryDtoMapper {

    public static Country toDomain(CountryDto countryRequest) {

        return new Country(
                (int)countryRequest.getId(),
                countryRequest.getName(),
                countryRequest.getGentilic()
        );
    }
    public static CountryDto fromDomain(Country country) {

        var countryDto = new CountryDto(country.getName(), country.getGentilic());
        countryDto.setId(country.getId());

        return countryDto;
    }

    public static List<CountryDto> fromDomain(List<Country> countries) {

        return countries.stream()
                .map(CountryDtoMapper::fromDomain)
                .collect(Collectors.toList());
    }
}
