package com.renan.booksalesonline.adapters.repositories.data;

import com.renan.booksalesonline.adapters.repositories.entities.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageData extends JpaRepository<LanguageEntity, Integer> { }
