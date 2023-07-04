package com.renan.booksalesonline.adapters.repositories.mappers;

import com.renan.booksalesonline.adapters.repositories.entities.ImageEntity;
import com.renan.booksalesonline.domain.PublicationImage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ImageEntityMapper {

    public static PublicationImage toDomain(ImageEntity imageEntity) {

        if (imageEntity == null)
            return null;

        return new PublicationImage(
                imageEntity.getId(),
                imageEntity.getName(),
                imageEntity.getUrl(),
                imageEntity.getPublicationId()
        );
    }

    public static List<PublicationImage> toDomain(List<ImageEntity> imageEntities) {

        if (imageEntities == null)
            return Collections.emptyList();

        return imageEntities.stream()
                .map(ImageEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static ImageEntity fromDomain(PublicationImage publicationImage) {

        if (publicationImage == null)
            return null;

        return new ImageEntity(
                publicationImage.getId(),
                publicationImage.getName(),
                publicationImage.getContentUrl(),
                publicationImage.getPublicationId()
        );
    }
}
