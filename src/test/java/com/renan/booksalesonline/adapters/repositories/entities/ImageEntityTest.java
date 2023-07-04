package com.renan.booksalesonline.adapters.repositories.entities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageEntityTest {

    @Test
    public void should_create_an_image_entity_instance() {

        var expected = new ImageEntity(99, "name", "url", 1);
        assertThat(expected.getId()).isEqualTo(99);
        assertThat(expected.getName()).isEqualTo("name");
        assertThat(expected.getUrl()).isEqualTo("url");
        assertThat(expected.getPublicationId()).isEqualTo(1);
    }
}
