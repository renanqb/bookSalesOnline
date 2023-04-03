package com.renan.booksalesonline.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_GATEWAY, reason="Validation errors occurred")
public class ValidationException extends RuntimeException {

}
