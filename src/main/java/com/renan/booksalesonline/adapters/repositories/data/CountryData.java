package com.renan.booksalesonline.adapters.repositories.data;

import com.renan.booksalesonline.adapters.repositories.entities.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryData extends JpaRepository<CountryEntity, Integer> { }
