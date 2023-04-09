package com.renan.booksalesonline.adapters.controllers.v1;

import com.renan.booksalesonline.adapters.repositories.CountryRepository;
import com.renan.booksalesonline.adapters.repositories.PublisherRepository;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.Publisher;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PublisherControllerFunctionalTest {

    @Autowired private CountryRepository countryRepository;
    @Autowired private PublisherRepository publisherRepository;
    @Autowired private PublisherController publisherController;

    private Country country = new Country(0, "brazil", "brazilian");
    private Publisher publisher1 = new Publisher(0, "publisher1", "history1", country);
    private Publisher publisher2 = new Publisher(0, "publisher2", "history2", country);
    private Publisher publisher3 = new Publisher(0, "publisher3", "history3", country);

    @BeforeAll
    @Transactional
    public void init() {

        countryRepository.save(country);
        publisherRepository.save(publisher1);
        publisherRepository.save(publisher2);
        publisherRepository.save(publisher3);
    }

    @Test
    @Order(1)
    public void should_get_all_countries_successfully() throws NoSuchMethodException {

        var publisherResponse = publisherController.getAllPublishers();

        assertThat(publisherResponse.size()).isEqualTo(3);
    }

    @AfterAll
    @Transactional
    public void tearDown() {

        publisherRepository.remove(publisher1);
        publisherRepository.remove(publisher2);
        publisherRepository.remove(publisher3);
        countryRepository.remove(country);
    }
}
