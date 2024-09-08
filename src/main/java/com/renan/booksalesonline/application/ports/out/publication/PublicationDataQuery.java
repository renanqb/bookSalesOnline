package com.renan.booksalesonline.application.ports.out.publication;

import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.domain.Publication;

public interface PublicationDataQuery extends DataQuery<Publication> {

    boolean existsPublicationByPublisherId(int publisherId);
}
