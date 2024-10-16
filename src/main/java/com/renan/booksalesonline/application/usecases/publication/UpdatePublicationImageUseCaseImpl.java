package com.renan.booksalesonline.application.usecases.publication;

import com.renan.booksalesonline.application.ports.in.common.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.publication.CreatePublicationImageUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publication.UpdatePublicationImageUseCase;
import com.renan.booksalesonline.application.ports.out.storage.StorageService;
import com.renan.booksalesonline.domain.PublicationImage;
import com.renan.booksalesonline.domain.exceptions.FileExtensionNotAccepted;
import com.renan.booksalesonline.domain.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
@AllArgsConstructor
public class UpdatePublicationImageUseCaseImpl implements UpdatePublicationImageUseCase {

    private final CreatePublicationImageUseCase createPublicationImageUseCase;
    private final RepositoryMediator repositoryMediator;
    private final StorageService storageService;

    @Override
    @Transactional
    public PublicationImage execute(@NotNull PublicationImage publicationImage, int id)
            throws IOException, NoSuchMethodException, URISyntaxException, FileExtensionNotAccepted {

        assert id > 0;

        var query = repositoryMediator.getQuery(PublicationImage.class);
        var persistedImage = query.getById(id);

        if (persistedImage == null) {
            throw new ValidationException(PublicationImage.class);
        }

        publicationImage.setId(id);
        publicationImage.setName(persistedImage.getName());
        publicationImage.setContentUrl(persistedImage.getContentUrl());
        publicationImage.setPublicationId(persistedImage.getPublicationId());

        return createPublicationImageUseCase
                .execute(persistedImage.getPublicationId(), publicationImage);
    }
}
