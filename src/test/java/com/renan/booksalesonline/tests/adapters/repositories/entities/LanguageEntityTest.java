package com.renan.booksalesonline.tests.adapters.repositories.entities;

import com.renan.booksalesonline.adapters.repositories.entities.LanguageEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LanguageEntityTest {

    @Test
    public void should_create_a_language_entity_instance() {

        var expected = new LanguageEntity(99, "portuguese");
        assertThat(expected.getId()).isEqualTo(99);
        assertThat(expected.getName()).isEqualTo("portuguese");
    }
}
