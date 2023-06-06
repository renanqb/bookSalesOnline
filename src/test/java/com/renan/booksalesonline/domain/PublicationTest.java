package com.renan.booksalesonline.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PublicationTest {

    @Test
    public void should_create_a_default_publication_domain_instance() {

        var publication = new Publication();
        assertThat((int)publication.getId()).isEqualTo(0);
        assertThat(publication.getName()).isEqualTo("");
        assertThat(publication.getPublisher()).isNull();
    }

    @Test
    public void should_create_a_publication_domain_instance() {

        var publisher = new Publisher(1, "publisher", "history", null);
        var publication = new Publication(99, "publication", publisher);
        assertThat((int)publication.getId()).isEqualTo(99);
        assertThat(publication.getName()).isEqualTo("publication");
        assertThat(publication.getPublisher()).isNotNull();
    }
}
