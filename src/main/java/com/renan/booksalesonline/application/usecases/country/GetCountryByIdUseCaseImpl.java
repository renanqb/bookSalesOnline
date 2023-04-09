package com.renan.booksalesonline.application.usecases.country;

import com.renan.booksalesonline.application.ports.in.country.GetCountryByIdUseCase;
import com.renan.booksalesonline.application.ports.out.country.CountryQuery;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetCountryByIdUseCaseImpl implements GetCountryByIdUseCase {

    private CountryQuery repository;

    public GetCountryByIdUseCaseImpl(@Autowired CountryQuery countryRepository) {
        this.repository = countryRepository;
    }

    @Override
    public Country execute(int id) throws NotFoundException {

        var foundCountry = repository.getById(id);

        if (foundCountry == null)
            throw new NotFoundException(Country.class, id);

        return foundCountry;
    }
}
