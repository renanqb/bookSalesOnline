package com.renan.booksalesonline.application.ports.in.country;

import com.renan.booksalesonline.domain.Country;

public interface UpdateCountryUseCase {
    
    Country execute(int id, Country country);
}
