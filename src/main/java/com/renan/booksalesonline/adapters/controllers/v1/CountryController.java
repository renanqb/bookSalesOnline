package com.renan.booksalesonline.adapters.controllers.v1;

import com.renan.booksalesonline.adapters.controllers.v1.mappers.CountryDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.model.CountryDto;
import com.renan.booksalesonline.application.ports.in.*;
import com.renan.booksalesonline.application.ports.in.commom.UseCaseMediator;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CountryController {

    private final UseCaseMediator mediator;

    public CountryController(@Autowired UseCaseMediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/countries")
    @ResponseStatus(value = HttpStatus.OK)
    public List<CountryDto> getAllCountries() throws NoSuchMethodException {

        var countries = mediator
                .get(GetAllEntitiesUseCase.class)
                .execute(Country.class);

        return CountryDtoMapper.fromDomain(countries);
    }

    @GetMapping("/countries/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public CountryDto getCountryById(@PathVariable("id") int id) throws NotFoundException, NoSuchMethodException {

        var country = mediator
                .get(GetEntityByIdUseCase.class)
                .execute(Country.class, id);

        return CountryDtoMapper.fromDomain(country);
    }

    @PostMapping("/countries")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CountryDto create(@RequestBody CountryDto countryRequest) throws NoSuchMethodException {

        var country = CountryDtoMapper.toDomain(countryRequest);
        var createdCountry = mediator
                .get(CreateEntityUseCase.class)
                .execute(Country.class, country);

        return CountryDtoMapper.fromDomain(createdCountry);
    }

    @PutMapping("/countries/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public CountryDto update(@PathVariable int id, @RequestBody CountryDto countryRequest) throws NoSuchMethodException {

        var country = CountryDtoMapper.toDomain(countryRequest);
        var createdCountry = mediator
                .get(UpdateEntityUseCase.class)
                .execute(Country.class, country, id);

        return CountryDtoMapper.fromDomain(createdCountry);
    }

    @DeleteMapping("/countries/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void remove(@PathVariable int id) throws NoSuchMethodException {

        mediator.get(RemoveEntityUseCase.class)
                .execute(Country.class, id);
    }
}
