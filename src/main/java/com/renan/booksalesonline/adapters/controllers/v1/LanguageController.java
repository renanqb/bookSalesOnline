package com.renan.booksalesonline.adapters.controllers.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renan.booksalesonline.adapters.controllers.v1.mappers.LanguageDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.model.LanguageDto;
import com.renan.booksalesonline.application.ports.in.common.UseCaseMediator;
import com.renan.booksalesonline.application.ports.in.usecases.*;
import com.renan.booksalesonline.domain.Language;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class LanguageController {

    private final UseCaseMediator mediator;

    @GetMapping("/languages")
    @ResponseStatus(value = HttpStatus.OK)
    public LanguageDto[] getAllCountries() throws NoSuchMethodException {

        var languages = mediator
                .get(GetAllEntitiesUseCase.class)
                .execute(Language.class);

        return LanguageDtoMapper.fromDomain(languages);
    }

    @GetMapping("/languages/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public LanguageDto getCountryById(@PathVariable("id") int id)
            throws NoSuchMethodException, JsonProcessingException {

        var country = mediator
                .get(GetEntityByIdUseCase.class)
                .execute(Language.class, id);

        return LanguageDtoMapper.fromDomain(country);
    }

    @PostMapping("/languages")
    @ResponseStatus(value = HttpStatus.CREATED)
    public LanguageDto create(@RequestBody LanguageDto languageRequest) throws NoSuchMethodException {

        var country = LanguageDtoMapper.toDomain(languageRequest);
        var createLanguage = mediator
                .get(CreateEntityUseCase.class)
                .execute(Language.class, country);

        return LanguageDtoMapper.fromDomain(createLanguage);
    }

    @PutMapping("/languages/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public LanguageDto update(@PathVariable int id,@RequestBody LanguageDto languageRequest)
            throws NoSuchMethodException {

        var language = LanguageDtoMapper.toDomain(languageRequest);
        var createdLanguage = mediator
                .get(UpdateEntityUseCase.class)
                .execute(Language.class, language, id);

        return LanguageDtoMapper.fromDomain(createdLanguage);
    }

    @DeleteMapping("/languages/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void remove(@PathVariable int id) throws NoSuchMethodException {

        var useCase = mediator.get(RemoveEntityUseCase.class);
        useCase.execute(Language.class, id);
    }
}
