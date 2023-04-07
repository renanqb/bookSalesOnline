package com.renan.booksalesonline.adapters.controllers.v1;

import com.renan.booksalesonline.adapters.controllers.v1.mappers.CountryDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.model.CountryDto;
import com.renan.booksalesonline.application.mediators.UseCaseMediator;
import com.renan.booksalesonline.application.mediators.UseCaseType;
import com.renan.booksalesonline.application.ports.in.country.*;
import com.renan.booksalesonline.domain.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CountryController {

    private UseCaseMediator mediator;

    public CountryController(UseCaseMediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/countries")
    @ResponseStatus(value = HttpStatus.OK)
    public List<CountryDto> getAllCountries() throws NoSuchMethodException {

        var countries = mediator
                .<GetAllCountriesUseCase>get(UseCaseType.COUNTRY_GET_ALL)
                .execute();

        return CountryDtoMapper.fromDomain(countries);
    }

    @GetMapping("/countries/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public CountryDto getCountryById(@PathVariable("id") int id) throws NotFoundException, NoSuchMethodException {

        var country = mediator
                .<GetCountryByIdUseCase>get(UseCaseType.COUNTRY_GET_BY_ID)
                .execute(id);

        return CountryDtoMapper.fromDomain(country);
    }

    @PostMapping("/countries")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CountryDto create(@RequestBody CountryDto countryRequest) throws NoSuchMethodException {

        var country = CountryDtoMapper.toDomain(countryRequest);
        var createdCountry = mediator
                .<CreateCountryUseCase>get(UseCaseType.COUNTRY_CREATE)
                .execute(country);

        return CountryDtoMapper.fromDomain(createdCountry);
    }

    @PutMapping("/countries/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public CountryDto update(@PathVariable int id, @RequestBody CountryDto countryRequest) throws NoSuchMethodException {

        var country = CountryDtoMapper.toDomain(countryRequest);
        var createdCountry = mediator
                .<UpdateCountryUseCase>get(UseCaseType.COUNTRY_UPDATE)
                .execute(id, country);

        return CountryDtoMapper.fromDomain(createdCountry);
    }

    @DeleteMapping("/countries/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void remove(@PathVariable int id) throws NoSuchMethodException {

        mediator.<RemoveCountryUseCase>get(UseCaseType.COUNTRY_REMOVE)
                .execute(id);
    }
}
