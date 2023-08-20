package com.renan.booksalesonline.application.usecases.publication;

import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.publication.CreatePublicationImageUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publication.UpdatePublicationImageUseCase;
import com.renan.booksalesonline.application.ports.out.storage.StorageService;
import com.renan.booksalesonline.domain.PublicationImage;
import com.renan.booksalesonline.domain.exceptions.FileExtensionNotAccepted;
import com.renan.booksalesonline.domain.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class UpdatePublicationImageUseCaseImpl implements UpdatePublicationImageUseCase {

    private CreatePublicationImageUseCase createPublicationImageUseCase;
    private RepositoryMediator repositoryMediator;
    private StorageService storageService;

    public UpdatePublicationImageUseCaseImpl(
            @Autowired CreatePublicationImageUseCase createPublicationImageUseCase,
            @Autowired RepositoryMediator repositoryMediator,
            @Autowired StorageService storageService
    ) {

        this.createPublicationImageUseCase = createPublicationImageUseCase;
        this.repositoryMediator = repositoryMediator;
        this.storageService = storageService;
    }

    @Override
    @Transactional
    public PublicationImage execute(PublicationImage publicationImage, int id)
            throws IOException, NoSuchMethodException, URISyntaxException, FileExtensionNotAccepted {

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
