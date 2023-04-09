package com.renan.booksalesonline.application.usecases.country;

import com.renan.booksalesonline.application.ports.in.country.CreateCountryUseCase;
import com.renan.booksalesonline.application.ports.out.country.CountryCommand;
import com.renan.booksalesonline.domain.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateCountryUseCaseImpl implements CreateCountryUseCase {

    private CountryCommand repository;

    public CreateCountryUseCaseImpl(@Autowired CountryCommand countryRepository) {
        this.repository = countryRepository;
    }
    @Override
    public Country execute(Country country) {

        return repository.save(country);
    }
}
