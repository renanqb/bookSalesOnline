package com.renan.booksalesonline.adapters.controllers.v1;

import com.renan.booksalesonline.adapters.controllers.v1.mappers.CountryResponseMapper;
import com.renan.booksalesonline.adapters.controllers.v1.model.CountryResponse;
import com.renan.booksalesonline.application.mediators.UseCaseMediator;
import com.renan.booksalesonline.application.mediators.UseCaseType;
import com.renan.booksalesonline.application.ports.in.GetAllCountriesUseCase;
import com.renan.booksalesonline.application.ports.in.GetCountryByIdUseCase;
import com.renan.booksalesonline.domain.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CountryController {

    private UseCaseMediator mediator;

    public CountryController(UseCaseMediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/countries")
    public List<CountryResponse> getAllCountries() throws NoSuchMethodException {

        var countries = mediator
                .<GetAllCountriesUseCase>get(UseCaseType.COUNTRY_GET_ALL)
                .execute();

        return CountryResponseMapper.fromDomain(countries);
    }

    @GetMapping("/countries/{id}")
    public CountryResponse getCountryById(@PathVariable("id") int id) throws NotFoundException, NoSuchMethodException {

        var country = mediator
                .<GetCountryByIdUseCase>get(UseCaseType.COUNTRY_GET_BY_ID)
                .execute(id);

        return CountryResponseMapper.fromDomain(country);
    }
}
