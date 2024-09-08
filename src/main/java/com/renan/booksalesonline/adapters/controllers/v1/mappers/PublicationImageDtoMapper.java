package com.renan.booksalesonline.adapters.controllers.v1.mappers;

import com.renan.booksalesonline.adapters.controllers.v1.model.PublicationImageDto;
import com.renan.booksalesonline.domain.PublicationImage;
import com.renan.booksalesonline.domain.PublicationImageContent;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class PublicationImageDtoMapper {

    public static PublicationImage toDomain(MultipartFile file) throws IOException {

        var imageContent = new PublicationImageContent(
                file.getOriginalFilename(),
                file.getSize(),
                file.getInputStream()
        );
        return new PublicationImage(imageContent);
    }

    public static PublicationImageDto fromDomain(PublicationImage publicationImage) {

        var publicationImageDto = new PublicationImageDto(
                publicationImage.getName(),
                publicationImage.getContentUrl(),
                publicationImage.getPublicationId()
        );
        publicationImageDto.setId(publicationImage.getId());

        return publicationImageDto;
    }

    public static PublicationImageDto[] fromDomain(List<PublicationImage> publicationImages) {

        return publicationImages.stream()
                .map(PublicationImageDtoMapper::fromDomain)
                .toArray(PublicationImageDto[]::new);
    }
}
