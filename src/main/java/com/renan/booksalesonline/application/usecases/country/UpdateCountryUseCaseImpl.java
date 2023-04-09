package com.renan.booksalesonline.application.usecases.country;

import com.renan.booksalesonline.application.ports.in.country.UpdateCountryUseCase;
import com.renan.booksalesonline.application.ports.out.country.CountryCommand;
import com.renan.booksalesonline.application.ports.out.country.CountryQuery;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.exceptions.UpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateCountryUseCaseImpl implements UpdateCountryUseCase {

    private CountryCommand countryCommRepository;
    private CountryQuery countryQueryRepository;

    public UpdateCountryUseCaseImpl(
            @Autowired CountryCommand countryCommRepository,
            @Autowired CountryQuery countryQueryRepository
    ) {
        this.countryCommRepository = countryCommRepository;
        this.countryQueryRepository = countryQueryRepository;
    }

    @Override
    public Country execute(int id, Country country) {

        var persistedCountry = countryQueryRepository.getById(id);

        if (persistedCountry == null)
            throw new UpdateException(Country.class, id);

        country.setId(id);

        return countryCommRepository.save(country);
    }
}
