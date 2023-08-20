package com.renan.booksalesonline.application.ports.in.usecases.publication;

import com.renan.booksalesonline.domain.PublicationImage;

import java.util.List;

public interface GetAllPublicationImagesUseCase {

    List<PublicationImage> execute(int publicationId) throws NoSuchMethodException;
}
