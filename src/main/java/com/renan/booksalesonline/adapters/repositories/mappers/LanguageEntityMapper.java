package com.renan.booksalesonline.adapters.repositories.mappers;

import com.renan.booksalesonline.adapters.repositories.entities.LanguageEntity;
import com.renan.booksalesonline.domain.Language;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LanguageEntityMapper {

    public static Language toDomain(LanguageEntity languageEntity) {

        if (languageEntity == null)
            return null;

        return new Language(languageEntity.getId(), languageEntity.getName());
    }

    public static List<Language> toDomain(List<LanguageEntity> languageEntities) {

        if (languageEntities == null)
            return Collections.emptyList();

        return languageEntities.stream()
                .map(LanguageEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static LanguageEntity fromDomain(Language language) {

        if (language == null)
            return null;

        return new LanguageEntity(language.getId(), language.getName());
    }
}
