package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.adapters.repositories.entities.CountryEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LanguageTest {

    @Test
    public void should_create_a_language_domain_instance() {

        var language = new Language(99, "portuguese");
        assertThat((int)language.getId()).isEqualTo(99);
        assertThat(language.getName()).isEqualTo("portuguese");
    }
}
