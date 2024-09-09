package com.renan.booksalesonline.tests.domain.exceptions;

import com.renan.booksalesonline.domain.exceptions.ForbiddenException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ForbiddenExceptionTest {

    @Test
    public void should_create_a_forbidden_exception() {

        var exception = new ForbiddenException();

        assertThat(exception.getMessage())
                .isEqualTo("Solicited action is forbidden");
    }
}
