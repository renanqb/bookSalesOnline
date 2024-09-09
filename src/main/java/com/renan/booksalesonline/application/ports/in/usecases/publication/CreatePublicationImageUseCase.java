package com.renan.booksalesonline.application.ports.in.usecases.publication;

import com.renan.booksalesonline.domain.PublicationImage;
import com.renan.booksalesonline.domain.exceptions.FileExtensionNotAccepted;

import java.io.IOException;
import java.net.URISyntaxException;

public interface CreatePublicationImageUseCase {

    PublicationImage execute(int publicationId, PublicationImage publicationImage)
            throws IOException, NoSuchMethodException, URISyntaxException, FileExtensionNotAccepted;
}
