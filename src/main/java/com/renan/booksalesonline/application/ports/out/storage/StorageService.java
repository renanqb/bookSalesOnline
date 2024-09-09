package com.renan.booksalesonline.application.ports.out.storage;

import com.renan.booksalesonline.domain.PublicationImage;

import java.io.IOException;
import java.net.URISyntaxException;

public interface StorageService {

    String save(String bucket, PublicationImage publicationImage) throws IOException, URISyntaxException;
}
