package com.renan.booksalesonline.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN, reason="Solicited action is forbidden")
public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super("Solicited action is forbidden");
    }
}
