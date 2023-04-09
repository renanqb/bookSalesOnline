package com.renan.booksalesonline.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE, reason="Problem to update item")
public class UpdateException extends RuntimeException {

    public UpdateException(Class<? extends Object> domainClass, int id) {
        super(String.format("%s with id = %s not exists to be updated", domainClass.getSimpleName(), id));
    }
}
