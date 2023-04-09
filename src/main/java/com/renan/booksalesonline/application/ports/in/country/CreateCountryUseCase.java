package com.renan.booksalesonline.application.ports.in.country;

import com.renan.booksalesonline.domain.Country;

public interface CreateCountryUseCase {
    
    Country execute(Country country);
}
