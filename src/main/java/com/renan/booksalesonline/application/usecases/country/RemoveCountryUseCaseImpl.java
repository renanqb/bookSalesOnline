package com.renan.booksalesonline.application.usecases.country;

import com.renan.booksalesonline.application.ports.in.country.RemoveCountryUseCase;
import com.renan.booksalesonline.application.ports.out.country.CountryCommand;
import com.renan.booksalesonline.application.ports.out.country.CountryQuery;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.exceptions.RemoveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoveCountryUseCaseImpl implements RemoveCountryUseCase {

    private CountryCommand countryCommRepository;
    private CountryQuery countryQueryRepository;

    public RemoveCountryUseCaseImpl(
            @Autowired CountryCommand countryCommRepository,
            @Autowired CountryQuery countryQueryRepository
    ) {
        this.countryCommRepository = countryCommRepository;
        this.countryQueryRepository = countryQueryRepository;
    }

    @Override
    public void execute(int id) {

        var persistedCountry = countryQueryRepository.getById(id);

        if (persistedCountry == null)
            throw new RemoveException(Country.class, id);

        countryCommRepository.remove(persistedCountry);
    }
}
