package com.renan.booksalesonline.adapters.repositories.data;

import com.renan.booksalesonline.adapters.repositories.entities.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectData extends JpaRepository<SubjectEntity, Integer> {
}
