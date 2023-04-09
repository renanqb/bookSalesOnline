package com.renan.booksalesonline.adapters.controllers.v1;

import com.renan.booksalesonline.adapters.controllers.v1.mappers.PublisherDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.model.PublisherDto;
import com.renan.booksalesonline.application.mediators.UseCaseMediatorImpl;
import com.renan.booksalesonline.application.ports.in.publisher.GetAllPublishersUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PublisherController {

    private UseCaseMediatorImpl mediator;

    public PublisherController(UseCaseMediatorImpl mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/publishers")
    @ResponseStatus(value = HttpStatus.OK)
    public List<PublisherDto> getAllPublishers() throws NoSuchMethodException {

        var publishers = mediator
                .get(GetAllPublishersUseCase.class)
                .execute();

        return PublisherDtoMapper.fromDomain(publishers);
    }
}
