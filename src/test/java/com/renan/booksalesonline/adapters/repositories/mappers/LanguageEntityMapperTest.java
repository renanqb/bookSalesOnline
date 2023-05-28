package com.renan.booksalesonline.adapters.repositories.mappers;

import com.renan.booksalesonline.adapters.repositories.entities.LanguageEntity;
import com.renan.booksalesonline.domain.Language;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LanguageEntityMapperTest {

    @Test
    public void should_return_null_when_language_entity_is_null() {

        assertThat(LanguageEntityMapper.toDomain((LanguageEntity) null))
                .usingRecursiveComparison()
                .isEqualTo(null);
    }

    @Test
    public void should_parse_language_entity_to_language_domain() {

        var languageEntity = new LanguageEntity(1, "name");
        var expectedLanguage = new Language(1, "name");
        var actualLanguage = LanguageEntityMapper.toDomain(languageEntity);

        assertThat(actualLanguage)
                .usingRecursiveComparison()
                .isEqualTo(expectedLanguage);
    }

    @Test
    public void should_return_null_when_language_domain_is_null() {

        assertThat(LanguageEntityMapper.fromDomain(null))
                .usingRecursiveComparison()
                .isEqualTo(null);
    }

    @Test
    public void should_parse_country_domain_to_country_entity() {

        var language = new Language(1, "name");
        var expectedLanguageEntity = new LanguageEntity(1, "name");
        var actualLanguageEntity = LanguageEntityMapper.fromDomain(language);

        assertThat(actualLanguageEntity)
                .usingRecursiveComparison()
                .isEqualTo(expectedLanguageEntity);
    }

    @Test
    public void should_return_language_empty_list_when_domain_list_is_null() {

        assertThat(LanguageEntityMapper.toDomain((List<LanguageEntity>) null))
                .usingRecursiveComparison()
                .isEqualTo(Collections.EMPTY_LIST);
    }

    @Test
    public void should_parse_country_entities_to_countries() {

        var languageEntities = Arrays.asList(
                new LanguageEntity(1, "name1"),
                new LanguageEntity(2, "name2"),
                new LanguageEntity(3, "name3")
        );
        var expectedLanguageList = Arrays.asList(
                new Language(1, "name1"),
                new Language(2, "name2"),
                new Language(3, "name3")
        );
        var actual = LanguageEntityMapper.toDomain(languageEntities);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expectedLanguageList);
    }
}
