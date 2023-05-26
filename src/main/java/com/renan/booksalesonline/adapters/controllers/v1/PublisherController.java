package com.renan.booksalesonline.adapters.controllers.v1;

import com.renan.booksalesonline.adapters.controllers.v1.mappers.PublisherDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.model.PublisherDto;
import com.renan.booksalesonline.application.mediators.UseCaseMediatorImpl;
import com.renan.booksalesonline.application.ports.in.GetAllEntitiesUseCase;
import com.renan.booksalesonline.application.ports.in.GetEntityByIdUseCase;
import com.renan.booksalesonline.domain.Publisher;
import com.renan.booksalesonline.domain.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PublisherController {

    private final UseCaseMediatorImpl mediator;

    public PublisherController(UseCaseMediatorImpl mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/publishers")
    @ResponseStatus(value = HttpStatus.OK)
    public List<PublisherDto> getAllPublishers() throws NoSuchMethodException {

        var publishers = mediator
                .get(GetAllEntitiesUseCase.class)
                .execute(Publisher.class);

        return PublisherDtoMapper.fromDomain(publishers);
    }

    @GetMapping("/publishers/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public PublisherDto getPublisherById(@PathVariable("id") int id) throws NotFoundException, NoSuchMethodException {

        var publisher = mediator
                .get(GetEntityByIdUseCase.class)
                .execute(Publisher.class, id);

        return PublisherDtoMapper.fromDomain(publisher);
    }
}
