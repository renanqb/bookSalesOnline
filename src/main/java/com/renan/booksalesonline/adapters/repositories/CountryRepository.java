package com.renan.booksalesonline.adapters.repositories;

import com.renan.booksalesonline.adapters.repositories.data.CountryData;
import com.renan.booksalesonline.adapters.repositories.mappers.CountryEntityMapper;
import com.renan.booksalesonline.application.ports.out.DataCommand;
import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.domain.Country;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class CountryRepository implements DataQuery<Country>, DataCommand<Country> {

    private final CountryData countryData;

    @Override
    public List<Country> getAll() {

        var countryEntities = countryData.findAll();
        return CountryEntityMapper.toDomain(countryEntities);
    }

    @Override
    public Country getById(int id) {

        var optCountryEntity = countryData.findById(id);
        return optCountryEntity
                .map(CountryEntityMapper::toDomain)
                .orElse(null);
    }

    @Override
    public Country save(Country country) {

        var countryEntity = CountryEntityMapper.fromDomain(country);
        countryData.save(countryEntity);
        country.setId(countryEntity.getId());
        return country;
    }

    @Override
    public void remove(Country country) {

        var countryEntity = CountryEntityMapper.fromDomain(country);
        countryData.delete(countryEntity);
    }
}
