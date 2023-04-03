package com.renan.booksalesonline.adapters.repositories;

import com.renan.booksalesonline.adapters.repositories.data.CountryData;
import com.renan.booksalesonline.adapters.repositories.mappers.CountryEntityMapper;
import com.renan.booksalesonline.application.ports.out.CountryCommand;
import com.renan.booksalesonline.application.ports.out.CountryQuery;
import com.renan.booksalesonline.domain.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CountryRepository implements CountryQuery, CountryCommand {

    private CountryData countryData;

    public CountryRepository(@Autowired CountryData countryData) {
        this.countryData = countryData;
    }

    @Override
    public List<Country> getAll() {

        return CountryEntityMapper.toDomain(countryData.findAll());
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
        return CountryEntityMapper.toDomain(countryEntity);
    }

    @Override
    public void remove(Object id) {

        var countryEntity = countryData.findById((int)id).get();
        countryData.delete(countryEntity);
    }
}
