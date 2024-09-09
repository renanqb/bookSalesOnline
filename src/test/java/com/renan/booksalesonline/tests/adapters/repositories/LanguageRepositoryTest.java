package com.renan.booksalesonline.tests.adapters.repositories;

import com.renan.booksalesonline.adapters.repositories.LanguageRepository;
import com.renan.booksalesonline.adapters.repositories.data.LanguageData;
import com.renan.booksalesonline.adapters.repositories.entities.CountryEntity;
import com.renan.booksalesonline.adapters.repositories.entities.LanguageEntity;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LanguageRepositoryTest {

    @Mock private LanguageData languageData;
    @InjectMocks private LanguageRepository languageRepository;

    @Test
    public void should_get_all_language_entities() {

        var languageEntities = Arrays.asList(
                new LanguageEntity(1, "name1"),
                new LanguageEntity(2, "name2"),
                new LanguageEntity(3, "name3")
        );
        when(languageData.findAll()).thenReturn(languageEntities);

        var expectedLanguages = Arrays.asList(
                new Language(1, "name1"),
                new Language(2, "name2"),
                new Language(3, "name3")
        );

        var languages = languageRepository.getAll();

        for (int i = 0; i < 3; i++) {
            var expected = expectedLanguages.get(i);
            var actual = languages.get(i);

            assertThat((int)actual.getId()).isEqualTo((int)expected.getId());
            assertThat(actual.getName()).isEqualTo(expected.getName());
        }
    }

    @Test
    public void should_get_by_id_one_language_entity() {

        var languageEntity = new LanguageEntity(1, "name1");
        when(languageData.findById(anyInt())).thenReturn(Optional.of(languageEntity));

        var expectedLanguage = new Language(1, "name1");
        var actualLanguage = languageRepository.getById(1);

        assertThat((int)actualLanguage.getId()).isEqualTo((int)expectedLanguage.getId());
        assertThat(actualLanguage.getName()).isEqualTo(expectedLanguage.getName());
    }

    @Test
    public void should_create_a_language_entity() {

        var languageEntity = new LanguageEntity(1, "name1");
        when(languageData.save(any(LanguageEntity.class))).thenReturn(languageEntity);

        var expectedLanguage = new Language(1, "name1");
        var actualLanguage = languageRepository.save(expectedLanguage);

        assertThat((int)actualLanguage.getId()).isEqualTo((int)expectedLanguage.getId());
        assertThat(actualLanguage.getName()).isEqualTo(expectedLanguage.getName());
    }

    @Test
    public void should_remove_a_language_entity() {

        doNothing().when(languageData).delete(any(LanguageEntity.class));

        assertDoesNotThrow(() -> {
            var language = new Language(1, "name1");
            languageRepository.remove(language);
        });
    }
}
