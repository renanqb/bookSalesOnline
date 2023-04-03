package com.renan.booksalesonline.application.ports.in;

import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.exceptions.NotFoundException;

public interface GetCountryByIdUseCase {
    Country execute(int id) throws NotFoundException;
}
