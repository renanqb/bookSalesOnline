package com.renan.booksalesonline.adapters.repositories.data;

import com.renan.booksalesonline.adapters.repositories.entities.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherData extends JpaRepository<PublisherEntity, Integer> {

}
