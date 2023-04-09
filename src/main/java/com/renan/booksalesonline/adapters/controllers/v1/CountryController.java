package com.renan.booksalesonline.adapters.controllers.v1;

import com.renan.booksalesonline.adapters.controllers.v1.mappers.CountryDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.model.CountryDto;
import com.renan.booksalesonline.application.mediators.UseCaseMediatorImpl;
import com.renan.booksalesonline.application.mediators.UseCaseType;
import com.renan.booksalesonline.application.ports.in.country.*;
import com.renan.booksalesonline.domain.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CountryController {

    private UseCaseMediatorImpl mediator;

    public CountryController(UseCaseMediatorImpl mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/countries")
    @ResponseStatus(value = HttpStatus.OK)
    public List<CountryDto> getAllCountries() throws NoSuchMethodException {

        var countries = mediator
                .get(GetAllCountriesUseCase.class)
                .execute();

        return CountryDtoMapper.fromDomain(countries);
    }

    @GetMapping("/countries/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public CountryDto getCountryById(@PathVariable("id") int id) throws NotFoundException, NoSuchMethodException {

        var country = mediator
                .get(GetCountryByIdUseCase.class)
                .execute(id);

        return CountryDtoMapper.fromDomain(country);
    }

    @PostMapping("/countries")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CountryDto create(@RequestBody CountryDto countryRequest) throws NoSuchMethodException {

        var country = CountryDtoMapper.toDomain(countryRequest);
        var createdCountry = mediator
                .get(CreateCountryUseCase.class)
                .execute(country);

        return CountryDtoMapper.fromDomain(createdCountry);
    }

    @PutMapping("/countries/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public CountryDto update(@PathVariable int id, @RequestBody CountryDto countryRequest) throws NoSuchMethodException {

        var country = CountryDtoMapper.toDomain(countryRequest);
        var createdCountry = mediator
                .get(UpdateCountryUseCase.class)
                .execute(id, country);

        return CountryDtoMapper.fromDomain(createdCountry);
    }

    @DeleteMapping("/countries/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void remove(@PathVariable int id) throws NoSuchMethodException {

        mediator.get(RemoveCountryUseCase.class).execute(id);
    }
}
