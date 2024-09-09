package com.renan.booksalesonline.adapters.controllers.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renan.booksalesonline.adapters.controllers.v1.mappers.CountryDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.mappers.PublisherDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.model.CountryDto;
import com.renan.booksalesonline.adapters.controllers.v1.model.PublisherDto;
import com.renan.booksalesonline.application.ports.in.commom.UseCaseMediator;
import com.renan.booksalesonline.application.ports.in.usecases.CreateEntityUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.GetAllEntitiesUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.GetEntityByIdUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.UpdateEntityUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.country.GetPublishersByCountryUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.country.RemoveCountryUseCase;
import com.renan.booksalesonline.domain.Country;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class CountryController {

    private final UseCaseMediator mediator;

    @GetMapping("/countries")
    @ResponseStatus(value = HttpStatus.OK)
    public CountryDto[] getAllCountries() throws NoSuchMethodException {

        var countries = mediator
                .get(GetAllEntitiesUseCase.class)
                .execute(Country.class);

        return CountryDtoMapper.fromDomain(countries);
    }

    @GetMapping("/countries/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @Cacheable(value = "countryDto", key = "#id")
    public CountryDto getCountryById(@PathVariable("id") int id)
            throws NoSuchMethodException, JsonProcessingException {

        var country = mediator
                .get(GetEntityByIdUseCase.class)
                .execute(Country.class, id);

        return CountryDtoMapper.fromDomain(country);
    }

    @GetMapping("/countries/{id}/publishers")
    @ResponseStatus(value = HttpStatus.OK)
    public PublisherDto[] getPublishersByCountryId(@PathVariable("id") int countryId)
            throws NoSuchMethodException {

        var publishers = mediator
                .get(GetPublishersByCountryUseCase.class)
                .execute(countryId);

        return PublisherDtoMapper.fromDomain(publishers);
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
    public CountryDto update(@PathVariable int id, @RequestBody CountryDto countryRequest)
            throws NoSuchMethodException {

        var country = CountryDtoMapper.toDomain(countryRequest);
        var createdCountry = mediator
                .get(UpdateEntityUseCase.class)
                .execute(Country.class, country, id);

        return CountryDtoMapper.fromDomain(createdCountry);
    }

    @DeleteMapping("/countries/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void remove(@PathVariable int id) throws NoSuchMethodException {

        var useCase = mediator.get(RemoveCountryUseCase.class);
        useCase.execute(id);
    }
}
