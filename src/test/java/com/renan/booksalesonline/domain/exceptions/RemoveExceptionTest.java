package com.renan.booksalesonline.domain.exceptions;

import com.renan.booksalesonline.domain.Country;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RemoveExceptionTest {

    @Test
    public void should_create_a_remove_exception() {

        var exception = new RemoveException(Country.class, 99);

        assertThat(exception.getMessage())
                .isEqualTo("Country with id = 99 not exists to be removed");
    }
}
