package com.renan.booksalesonline.adapters.controllers.v1;

import com.renan.booksalesonline.adapters.controllers.v1.mappers.CountryDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.mappers.LanguageDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.model.CountryDto;
import com.renan.booksalesonline.adapters.controllers.v1.model.LanguageDto;
import com.renan.booksalesonline.application.ports.in.*;
import com.renan.booksalesonline.application.ports.in.commom.UseCaseMediator;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.Language;
import com.renan.booksalesonline.domain.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LanguageController {

    private final UseCaseMediator mediator;

    public LanguageController(@Autowired UseCaseMediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/languages")
    @ResponseStatus(value = HttpStatus.OK)
    public List<LanguageDto> getAllCountries() throws NoSuchMethodException {

        var languages = mediator
                .get(GetAllEntitiesUseCase.class)
                .execute(Language.class);

        return LanguageDtoMapper.fromDomain(languages);
    }

    @GetMapping("/languages/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public LanguageDto getCountryById(
            @PathVariable("id") int id
    ) throws NotFoundException, NoSuchMethodException {

        var country = mediator
                .get(GetEntityByIdUseCase.class)
                .execute(Language.class, id);

        return LanguageDtoMapper.fromDomain(country);
    }

    @PostMapping("/languages")
    @ResponseStatus(value = HttpStatus.CREATED)
    public LanguageDto create(
            @RequestBody LanguageDto languageRequest
    ) throws NoSuchMethodException {

        var country = LanguageDtoMapper.toDomain(languageRequest);
        var createLanguage = mediator
                .get(CreateEntityUseCase.class)
                .execute(Language.class, country);

        return LanguageDtoMapper.fromDomain(createLanguage);
    }

    @PutMapping("/languages/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public LanguageDto update(
            @PathVariable int id,
            @RequestBody LanguageDto languageRequest
    ) throws NoSuchMethodException {

        var language = LanguageDtoMapper.toDomain(languageRequest);
        var createdLanguage = mediator
                .get(UpdateEntityUseCase.class)
                .execute(Language.class, language, id);

        return LanguageDtoMapper.fromDomain(createdLanguage);
    }

    @DeleteMapping("/languages/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void remove(@PathVariable int id) throws NoSuchMethodException {

        mediator.get(RemoveEntityUseCase.class)
                .execute(Language.class, id);
    }
}