package com.renan.booksalesonline.application.mediators;

import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.out.DataCommand;
import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.application.ports.out.base.DataCommandQuery;
import com.renan.booksalesonline.application.ports.out.image.ImageDataQuery;
import com.renan.booksalesonline.application.ports.out.publication.PublicationDataQuery;
import com.renan.booksalesonline.application.ports.out.publisher.PublisherDataQuery;
import com.renan.booksalesonline.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class RepositoryMediatorImpl implements RepositoryMediator {

    private final HashMap<Class, DataCommandQuery> queries = new HashMap<>();
    private final HashMap<Class, DataCommandQuery> commands = new HashMap<>();

    public RepositoryMediatorImpl(
            @Autowired DataQuery<Country> countryQuery,
            @Autowired DataCommand<Country> countryCommand,
            @Autowired PublisherDataQuery publisherQuery,
            @Autowired DataCommand<Publisher> publisherCommand,
            @Autowired PublicationDataQuery publicationDataQuery,
            @Autowired DataQuery<Language> languageQuery,
            @Autowired DataCommand<Language> languageCommand,
            @Autowired ImageDataQuery imageQuery,
            @Autowired DataCommand<PublicationImage> imageCommand) {

        queries.put(Country.class, countryQuery);
        queries.put(Publisher.class, publisherQuery);
        queries.put(Publication.class, publicationDataQuery);
        queries.put(Language.class, languageQuery);
        queries.put(PublicationImage.class, imageQuery);

        commands.put(Country.class, countryCommand);
        commands.put(Publisher.class, publisherCommand);
        commands.put(Language.class, languageCommand);
        commands.put(PublicationImage.class, imageCommand);
    }

    @Override
    public <T> DataQuery<T> getQuery(Class<T> clazz) throws NoSuchMethodException {

        return (DataQuery<T>) get(clazz, queries);
    }

    @Override
    public <T> DataCommand<T> getCommand(Class<T> clazz) throws NoSuchMethodException {

        return (DataCommand<T>) get(clazz, commands);
    }

    private <T> T get(Class<T> clazz, HashMap<Class, DataCommandQuery> services) throws NoSuchMethodException {

        var repository = services.get(clazz);
        if (repository == null) {
            throw new NoSuchMethodException("There is no repository provider");
        }
        return (T) repository;
    }
}
