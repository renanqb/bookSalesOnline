package com.renan.booksalesonline.adapters.controllers.v1;

import com.renan.booksalesonline.adapters.controllers.v1.model.CountryDto;
import com.renan.booksalesonline.adapters.controllers.v1.model.PublisherDto;
import com.renan.booksalesonline.adapters.repositories.CountryRepository;
import com.renan.booksalesonline.adapters.repositories.PublisherRepository;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.Publisher;
import com.renan.booksalesonline.testhelpers.RestClientTesting;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PublisherControllerFunctionalTest {

    @Autowired private RestClientTesting restClientTesting;
    @Autowired private CountryRepository countryRepository;
    @Autowired private PublisherRepository publisherRepository;
    @Autowired private PublisherController publisherController;
    private Country brazil = new Country(0, "brazil", "brazilian");
    private Publisher publisher1 = new Publisher(0, "publisher1", "history1", brazil);
    private Publisher publisher2 = new Publisher(0, "publisher2", "history2", brazil);
    private Publisher publisher3 = new Publisher(0, "publisher3", "history3", brazil);
    private int createdPublisherId = 0;
    private String basePath = "publishers";

    @BeforeAll
    @Transactional
    public void init() {

        countryRepository.save(brazil);
        publisherRepository.save(publisher1);
        publisherRepository.save(publisher2);
        publisherRepository.save(publisher3);
    }

    @Test
    @Order(1)
    public void should_create_a_publisher_successfully() throws NoSuchMethodException {

        var countryDto = new CountryDto(brazil.getName(), brazil.getGentilic());
        countryDto.setId(brazil.getId());
        var publisher4 = new PublisherDto("publisher4_a", "history4_a", countryDto);
        var response = restClientTesting.post(PublisherDto.class, basePath, publisher4);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        var publisher = response.getBody();
        assertThat(publisher.getId()).isInstanceOfAny(Integer.class);

        createdPublisherId = (int) publisher.getId();
        assertThat(publisher.getName()).isEqualTo("publisher4_a");
        assertThat(publisher.getHistory()).isEqualTo("history4_a");
    }

    @Test
    @Order(2)
    public void should_update_created_publisher_successfully() throws NoSuchMethodException {

        var countryDto = new CountryDto(brazil.getName(), brazil.getGentilic());
        countryDto.setId(brazil.getId());
        var publisher4 = new PublisherDto("publisher4", "history4", countryDto);
        var response = restClientTesting.put(PublisherDto.class, basePath + "/" + createdPublisherId, publisher4);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var publisher = response.getBody();
        assertThat((int)publisher.getId()).isEqualTo(createdPublisherId);
        assertThat(publisher.getName()).isEqualTo("publisher4");
        assertThat(publisher.getHistory()).isEqualTo("history4");
    }

    @Test
    @Order(3)
    public void should_get_by_id_created_publisher_successfully() throws NoSuchMethodException {

        var response = restClientTesting.get(PublisherDto.class, basePath + "/" + createdPublisherId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var publisher = response.getBody();
        assertThat(publisher.getId()).isEqualTo(createdPublisherId);
        assertThat(publisher.getName()).isEqualTo("publisher4");
        assertThat(publisher.getHistory()).isEqualTo("history4");
        assertThat(publisher.getCountry().getId()).isEqualTo(brazil.getId());
    }

    @Test
    @Order(4)
    public void should_remove_created_publisher_successfully() {

        assertDoesNotThrow(() -> {
            restClientTesting.delete(basePath + "/" + createdPublisherId);
        });
    }

    @Test
    @Order(5)
    public void should_get_all_publishers_successfully() {

        var response = restClientTesting.get(PublisherDto[].class, basePath);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var publishers = response.getBody();
        assertThat(publishers.length).isEqualTo(3);
    }

    @AfterAll
    @Transactional
    public void tearDown() {

        publisherRepository.remove(publisher1);
        publisherRepository.remove(publisher2);
        publisherRepository.remove(publisher3);
        countryRepository.remove(brazil);
    }
}
