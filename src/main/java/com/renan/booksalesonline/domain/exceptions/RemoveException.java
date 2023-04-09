package com.renan.booksalesonline.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE, reason="Problem to remove item")
public class RemoveException extends RuntimeException {

    public RemoveException(Class<? extends Object> domainClass, int id) {
        super(String.format("%s with id = %s not exists to be removed", domainClass.getSimpleName(), id));
    }
}
