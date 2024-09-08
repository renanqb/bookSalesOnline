package com.renan.booksalesonline.tests.adapters.controllers.v1.model;

import com.renan.booksalesonline.adapters.controllers.v1.model.PublicationImageDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PublicationImageDtoTest {

    @Test
    public void should_create_a_default_publication_image_dto_instance() {

        var dto = new PublicationImageDto();

        assertThat(dto.getId()).isEqualTo(0);
        assertThat(dto.getName()).isNull();
        assertThat(dto.getUrl()).isNull();
        assertThat(dto.getPublicationId()).isEqualTo(0);
    }

    @Test
    public void should_create_a_publication_image_dto_instance() {

        var dto = new PublicationImageDto(
                "cc7af98f-9978-4cba-b3c6-8103874624e9.png",
                "http://localhost:4566/booksalesonline-images/cc7af98f-9978-4cba-b3c6-8103874624e9.jpg",
                1
        );

        assertThat(dto.getId()).isEqualTo(0);
        assertThat(dto.getName()).isEqualTo("cc7af98f-9978-4cba-b3c6-8103874624e9.png");
        assertThat(dto.getUrl()).isEqualTo(
                "http://localhost:4566/booksalesonline-images/cc7af98f-9978-4cba-b3c6-8103874624e9.jpg");
        assertThat(dto.getPublicationId()).isEqualTo(1);
    }
}
