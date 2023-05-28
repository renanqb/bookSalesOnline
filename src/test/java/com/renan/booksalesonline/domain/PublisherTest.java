package com.renan.booksalesonline.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PublisherTest {

    @Test
    public void should_create_a_publisher_domain_instance() {

        var country = new Country(1, "brazil", "");
        var publisher = new Publisher(99, "publisher", "history", country);
        assertThat((int)publisher.getId()).isEqualTo(99);
        assertThat(publisher.getName()).isEqualTo("publisher");
        assertThat(publisher.getHistory()).isEqualTo("history");
    }
}
