package com.renan.booksalesonline.adapters.controllers.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renan.booksalesonline.adapters.controllers.v1.mappers.PublisherDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.model.PublisherDto;
import com.renan.booksalesonline.application.ports.in.commom.UseCaseMediator;
import com.renan.booksalesonline.application.ports.in.usecases.GetAllEntitiesUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.GetEntityByIdUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publisher.CreatePublisherUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publisher.RemovePublisherUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publisher.UpdatePublisherUseCase;
import com.renan.booksalesonline.domain.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class PublisherController {

    private final UseCaseMediator mediator;

    public PublisherController(@Autowired UseCaseMediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/publishers")
    @ResponseStatus(value = HttpStatus.OK)
    public PublisherDto[] getAllPublishers() throws NoSuchMethodException {

        var publishers = mediator
                .get(GetAllEntitiesUseCase.class)
                .execute(Publisher.class);

        return PublisherDtoMapper.fromDomain(publishers);
    }

    @GetMapping("/publishers/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public PublisherDto getPublisherById(@PathVariable("id") int id)
            throws NoSuchMethodException, JsonProcessingException {

        var publisher = mediator
                .get(GetEntityByIdUseCase.class)
                .execute(Publisher.class, id);

        return PublisherDtoMapper.fromDomain(publisher);
    }

    @PostMapping("/publishers")
    @ResponseStatus(value = HttpStatus.CREATED)
    public PublisherDto create(@RequestBody PublisherDto publisherRequest) throws NoSuchMethodException {

        var publisher = PublisherDtoMapper.toDomain(publisherRequest);
        var publisherCreated = mediator
                .get(CreatePublisherUseCase.class)
                .execute(publisher);

        return PublisherDtoMapper.fromDomain(publisherCreated);
    }

    @PutMapping("/publishers/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public PublisherDto update(@PathVariable int id, @RequestBody PublisherDto publisherRequest)
            throws NoSuchMethodException {

        var publisher = PublisherDtoMapper.toDomain(publisherRequest);
        var createdPublisher = mediator
                .get(UpdatePublisherUseCase.class)
                .execute(publisher, id);

        return PublisherDtoMapper.fromDomain(createdPublisher);
    }

    @DeleteMapping("/publishers/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void remove(@PathVariable int id) throws NoSuchMethodException {

        mediator.get(RemovePublisherUseCase.class)
                .execute(id);
    }
}
