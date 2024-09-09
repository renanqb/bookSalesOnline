package com.renan.booksalesonline.adapters.repositories.data;

import com.renan.booksalesonline.adapters.repositories.entities.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageData extends JpaRepository<ImageEntity, Integer> {

    List<ImageEntity> getImagesByPublicationId(int publicationId);
}
