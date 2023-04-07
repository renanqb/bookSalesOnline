package com.renan.booksalesonline.domain.exceptions;

import com.renan.booksalesonline.domain.Country;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateExceptionTest {

    @Test
    public void should_create_a_update_exception() {

        var exception = new UpdateException(Country.class, 99);

        assertThat(exception.getMessage())
                .isEqualTo("Country with id = 99 not exists to be updated");
    }
}
