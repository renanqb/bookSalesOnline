package com.renan.booksalesonline.application.ports.out.image;

import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.domain.PublicationImage;

import java.util.List;

public interface ImageDataQuery extends DataQuery<PublicationImage> {

    List<PublicationImage> getImagesByPublicationId(int publicationId);
}
