package com.renan.booksalesonline.domain.exceptions;

import com.renan.booksalesonline.domain.Country;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationExceptionTest {

    @Test
    public void should_create_a_validation_exception() {

        var exception = new ValidationException(Country.class);

        assertThat(exception.getMessage())
                .isEqualTo("Country has validation problems");
    }
}
