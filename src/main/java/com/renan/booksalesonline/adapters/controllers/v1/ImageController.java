package com.renan.booksalesonline.adapters.controllers.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renan.booksalesonline.adapters.controllers.v1.mappers.PublicationImageDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.model.PublicationImageDto;
import com.renan.booksalesonline.application.ports.in.common.UseCaseMediator;
import com.renan.booksalesonline.application.ports.in.usecases.GetEntityByIdUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.RemoveEntityUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publication.UpdatePublicationImageUseCase;
import com.renan.booksalesonline.domain.PublicationImage;
import com.renan.booksalesonline.domain.exceptions.FileExtensionNotAccepted;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@AllArgsConstructor
public class ImageController {

    private final UseCaseMediator mediator;

    @GetMapping("/images/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public PublicationImageDto getPublicationImage(@PathVariable(name = "id") int id)
            throws NoSuchMethodException, JsonProcessingException {

        var publicationImage = mediator
                .get(GetEntityByIdUseCase.class)
                .execute(PublicationImage.class, id);

        return PublicationImageDtoMapper.fromDomain(publicationImage);
    }

    @PutMapping("/images/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public PublicationImageDto updateImage(
            @PathVariable(name = "id") int id,
            @RequestParam("file") MultipartFile file
    ) throws NoSuchMethodException, IOException, URISyntaxException, FileExtensionNotAccepted {

        var publicationImage = PublicationImageDtoMapper.toDomain(file);
        var createdpublicationImage = mediator
                .get(UpdatePublicationImageUseCase.class)
                .execute(publicationImage, id);

        return PublicationImageDtoMapper.fromDomain(createdpublicationImage);
    }

    @DeleteMapping("/images/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removePublicationImage(@PathVariable(name = "id") int id)
            throws NoSuchMethodException {

        mediator.get(RemoveEntityUseCase.class)
                .execute(PublicationImage.class, id);

    }
}
