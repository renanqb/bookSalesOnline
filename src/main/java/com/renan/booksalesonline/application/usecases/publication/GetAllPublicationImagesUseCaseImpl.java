package com.renan.booksalesonline.application.usecases.publication;

import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.publication.GetAllPublicationImagesUseCase;
import com.renan.booksalesonline.application.ports.out.image.ImageDataQuery;
import com.renan.booksalesonline.domain.PublicationImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllPublicationImagesUseCaseImpl implements GetAllPublicationImagesUseCase {

    private final RepositoryMediator repositoryMediator;

    public GetAllPublicationImagesUseCaseImpl(
            @Autowired RepositoryMediator repositoryMediator
    ) {
        this.repositoryMediator = repositoryMediator;
    }

    @Override
    public List<PublicationImage> execute(int publicationId) throws NoSuchMethodException {

        var query = (ImageDataQuery) repositoryMediator.getQuery(PublicationImage.class);
        return query.getImagesByPublicationId(publicationId);
    }
}
