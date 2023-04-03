package com.renan.booksalesonline.application.usecases;

import com.renan.booksalesonline.application.ports.in.GetAllCountriesUseCase;
import com.renan.booksalesonline.application.ports.out.CountryQuery;
import com.renan.booksalesonline.domain.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllCountriesUseCaseImpl implements GetAllCountriesUseCase {

    private CountryQuery repository;

    public GetAllCountriesUseCaseImpl(@Autowired CountryQuery countryRepository) {
        this.repository = countryRepository;
    }

    @Override
    public List<Country> execute() {
        return this.repository.getAll();
    }
}
