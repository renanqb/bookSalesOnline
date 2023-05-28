package com.renan.booksalesonline.adapters.controllers.v1.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LanguageDtoTest {

    @Test
    public void should_create_a_language_dto_instance() {

        var languageDto = new LanguageDto("portuguese");
        assertThat(languageDto.getId()).isEqualTo(0);
        assertThat(languageDto.getName()).isEqualTo("portuguese");
    }
}
