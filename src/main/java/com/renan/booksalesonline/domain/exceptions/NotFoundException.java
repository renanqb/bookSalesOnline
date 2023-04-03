package com.renan.booksalesonline.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Search return is empty")
public class NotFoundException extends RuntimeException {

    public NotFoundException(Class<? extends Object> domainClass, int id) {
        super(String.format("%s with id = %s can not be found", domainClass, id));
    }
}
