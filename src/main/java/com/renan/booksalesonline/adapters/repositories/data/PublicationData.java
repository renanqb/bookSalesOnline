package com.renan.booksalesonline.adapters.repositories.data;

import com.renan.booksalesonline.adapters.repositories.entities.PublicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicationData extends JpaRepository<PublicationEntity, Integer> {

    boolean existsPublicationByPublisherId(int publisherId);
}
