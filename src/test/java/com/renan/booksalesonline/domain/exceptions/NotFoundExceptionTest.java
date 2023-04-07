package com.renan.booksalesonline.domain.exceptions;

import com.renan.booksalesonline.domain.Country;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NotFoundExceptionTest {

    @Test
    public void should_create_a_not_found_exception() {

        var exception = new NotFoundException(Country.class, 99);

        assertThat(exception.getMessage())
                .isEqualTo("Country with id = 99 can not be found");
    }
}
