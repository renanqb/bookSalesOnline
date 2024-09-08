package com.renan.booksalesonline.adapters.repositories;

import com.renan.booksalesonline.adapters.repositories.data.ImageData;
import com.renan.booksalesonline.adapters.repositories.mappers.ImageEntityMapper;
import com.renan.booksalesonline.application.ports.out.DataCommand;
import com.renan.booksalesonline.application.ports.out.image.ImageDataQuery;
import com.renan.booksalesonline.domain.PublicationImage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class ImageRepository implements ImageDataQuery, DataCommand<PublicationImage> {

    private final ImageData imageData;

    @Override
    public PublicationImage save(PublicationImage publicationImage) {

        var imageEntity = ImageEntityMapper.fromDomain(publicationImage);
        imageData.save(imageEntity);
        publicationImage.setId(imageEntity.getId());

        return publicationImage;
    }

    @Override
    public void remove(PublicationImage publicationImage) {

        var imageEntity = ImageEntityMapper.fromDomain(publicationImage);
        imageData.delete(imageEntity);
    }

    @Override
    public List<PublicationImage> getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PublicationImage getById(int id) {

        var optImageEntity = imageData.findById(id);
        return optImageEntity
                .map(ImageEntityMapper::toDomain)
                .orElse(null);
    }

    @Override
    public List<PublicationImage> getImagesByPublicationId(int publicationId) {

        var imageEntities = imageData.getImagesByPublicationId(publicationId);
        return ImageEntityMapper.toDomain(imageEntities);
    }
}
