package com.renan.booksalesonline.adapters.controllers.v1;

import com.renan.booksalesonline.adapters.controllers.v1.mappers.PublicationImageDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.model.PublicationImageDto;
import com.renan.booksalesonline.application.ports.in.commom.UseCaseMediator;
import com.renan.booksalesonline.application.ports.in.usecases.publication.GetAllPublicationImagesUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publication.CreatePublicationImageUseCase;
import com.renan.booksalesonline.domain.exceptions.FileExtensionNotAccepted;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@AllArgsConstructor
public class PublicationController {

    private final UseCaseMediator mediator;

    @GetMapping("/publications/{publicationId}/images")
    @ResponseStatus(value = HttpStatus.OK)
    public PublicationImageDto[] getPublicationImages(
            @PathVariable(name = "publicationId") int publicationId
    ) throws NoSuchMethodException {

        var publicationsImages = mediator
                .get(GetAllPublicationImagesUseCase.class)
                .execute(publicationId);

        return PublicationImageDtoMapper.fromDomain(publicationsImages);
    }

    @PostMapping("/publications/{publicationId}/images")
    @ResponseStatus(value = HttpStatus.CREATED)
    public PublicationImageDto createPublicationImage(
            @PathVariable(name = "publicationId") int publicationId,
            @RequestParam("file") MultipartFile file
    ) throws NoSuchMethodException, IOException, URISyntaxException, FileExtensionNotAccepted {

        var publicationImage = PublicationImageDtoMapper.toDomain(file);
        var createdpublicationImage = mediator
                .get(CreatePublicationImageUseCase.class)
                .execute(publicationId, publicationImage);

        return PublicationImageDtoMapper.fromDomain(createdpublicationImage);
    }
}
