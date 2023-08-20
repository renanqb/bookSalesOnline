package com.renan.booksalesonline.tests.adapters.controllers.v1.mappers;

import com.renan.booksalesonline.adapters.controllers.v1.mappers.LanguageDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.model.LanguageDto;
import com.renan.booksalesonline.domain.Language;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class LanguageDtoMapperTest {

    @Test
    public void should_parse_language_dto_to_language_domain() {

        var languageDto = new LanguageDto("name");
        languageDto.setId(1);
        var expectedLanguage = new Language(1, "name");
        var actualLanguage = LanguageDtoMapper.toDomain(languageDto);

        assertThat(expectedLanguage)
                .usingRecursiveComparison()
                .isEqualTo(actualLanguage);
    }

    @Test
    public void should_parse_language_domain_to_country_entity() {

        var language = new Language(1, "name");
        var expectedLanguageDto = new LanguageDto("name");
        expectedLanguageDto.setId(1);
        var actualLanguageDto = LanguageDtoMapper.fromDomain(language);

        assertThat(actualLanguageDto)
                .usingRecursiveComparison()
                .isEqualTo(expectedLanguageDto);
    }

    @Test
    public void should_parse_language_entities_to_countries() {

        var languages = Arrays.asList(
                new Language(1, "name1"),
                new Language(2, "name2"),
                new Language(3, "name3")
        );
        var expectedLanguageDtos = new LanguageDto[] {
                new LanguageDto("name1"),
                new LanguageDto("name2"),
                new LanguageDto("name3")
        };
        expectedLanguageDtos[0].setId(1);
        expectedLanguageDtos[1].setId(2);
        expectedLanguageDtos[2].setId(3);

        var actualLanguageDtos = LanguageDtoMapper.fromDomain(languages);

        assertThat(actualLanguageDtos)
                .usingRecursiveComparison()
                .isEqualTo(expectedLanguageDtos);
    }
}
