package com.renan.booksalesonline.adapters.controllers.v1.mappers;

import com.renan.booksalesonline.adapters.controllers.v1.model.LanguageDto;
import com.renan.booksalesonline.adapters.controllers.v1.model.PublisherDto;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.Publisher;

import java.util.List;
import java.util.stream.Collectors;

public class PublisherDtoMapper {

    private PublisherDtoMapper() { }

    public static Publisher toDomain(PublisherDto publisherRequest) {

        var countryRequest = publisherRequest.getCountry();
        return new Publisher(
                (int) publisherRequest.getId(),
                publisherRequest.getName(),
                publisherRequest.getHistory(),
                new Country((int) countryRequest.getId())
        );
    }
    public static PublisherDto fromDomain(Publisher publisher) {

        var publisherDto = new PublisherDto(
                publisher.getName(),
                publisher.getHistory(),
                CountryDtoMapper.fromDomain(publisher.getCountry())
        );
        publisherDto.setId(publisher.getId());

        return publisherDto;
    }

    public static PublisherDto[] fromDomain(List<Publisher> publishers) {

        return publishers.stream()
                .map(PublisherDtoMapper::fromDomain)
                .toArray(PublisherDto[]::new);
    }
}
