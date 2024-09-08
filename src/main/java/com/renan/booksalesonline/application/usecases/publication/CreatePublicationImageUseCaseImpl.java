package com.renan.booksalesonline.application.usecases.publication;

import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.publication.CreatePublicationImageUseCase;
import com.renan.booksalesonline.application.ports.out.publication.PublicationDataQuery;
import com.renan.booksalesonline.application.ports.out.storage.StorageService;
import com.renan.booksalesonline.domain.PublicationImage;
import com.renan.booksalesonline.domain.Publication;
import com.renan.booksalesonline.domain.exceptions.FileExtensionNotAccepted;
import com.renan.booksalesonline.domain.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
@AllArgsConstructor
public class CreatePublicationImageUseCaseImpl implements CreatePublicationImageUseCase {

    private final RepositoryMediator repositoryMediator;
    private final StorageService storageService;

    @Override
    @Transactional
    public PublicationImage execute(int publicationId, PublicationImage publicationImage)
            throws IOException, NoSuchMethodException, URISyntaxException, FileExtensionNotAccepted {

        publicationImage.getImageContent().validate();

        var query = (PublicationDataQuery) repositoryMediator.getQuery(Publication.class);
        var publication = query.getById(publicationId);

        if (publication == null) {
            throw new ValidationException(PublicationImage.class);
        }

        var imageUrl = storageService.save("booksalesonline-images", publicationImage);
        publicationImage.setContentUrl(imageUrl);
        publicationImage.setPublicationId(publicationId);

        var command = repositoryMediator.getCommand(PublicationImage.class);

        return command.save(publicationImage);
    }
}
