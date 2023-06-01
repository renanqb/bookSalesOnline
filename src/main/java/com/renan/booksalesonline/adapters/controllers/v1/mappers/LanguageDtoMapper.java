package com.renan.booksalesonline.adapters.controllers.v1.mappers;

import com.renan.booksalesonline.adapters.controllers.v1.model.LanguageDto;
import com.renan.booksalesonline.domain.Language;

import java.util.List;

public class LanguageDtoMapper {

    private LanguageDtoMapper() { }

    public static Language toDomain(LanguageDto languageRequest) {

        return new Language((int)languageRequest.getId(), languageRequest.getName());
    }
    public static LanguageDto fromDomain(Language language) {

        var languageDto = new LanguageDto(language.getName());
        languageDto.setId(language.getId());

        return languageDto;
    }

    public static LanguageDto[] fromDomain(List<Language> languages) {

        return languages.stream()
                .map(LanguageDtoMapper::fromDomain)
                .toArray(LanguageDto[]::new);
    }
}
