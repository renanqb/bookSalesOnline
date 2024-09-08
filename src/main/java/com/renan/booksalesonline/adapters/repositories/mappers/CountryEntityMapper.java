package com.renan.booksalesonline.adapters.repositories.mappers;

import com.renan.booksalesonline.adapters.repositories.entities.CountryEntity;
import com.renan.booksalesonline.domain.Country;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CountryEntityMapper {

    public static Country toDomain(CountryEntity countryEntity) {

        if (countryEntity == null)
            return null;

        return new Country(
                countryEntity.getId(),
                countryEntity.getName(),
                countryEntity.getNationality()
        );
    }

    public static List<Country> toDomain(List<CountryEntity> countryEntities) {

        if (countryEntities == null)
            return Collections.emptyList();

        return countryEntities.stream()
                .map(CountryEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static CountryEntity fromDomain(Country country) {

        if (country == null)
            return null;

        return new CountryEntity(
                country.getId(),
                country.getName(),
                country.getNationality()
        );
    }
}
