package com.renan.booksalesonline.tests.adapters.controllers.v1;

import com.renan.booksalesonline.adapters.controllers.v1.CountryController;
import com.renan.booksalesonline.adapters.controllers.v1.model.CountryDto;
import com.renan.booksalesonline.adapters.repositories.CountryRepository;
import com.renan.booksalesonline.adapters.repositories.PublisherRepository;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.Publisher;
import com.renan.booksalesonline.tests.testhelpers.BookSalesOnlineContainerTest;
import com.renan.booksalesonline.tests.testhelpers.RestClientTesting;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CountryControllerFunctionalTest extends BookSalesOnlineContainerTest {

    @Autowired private RestClientTesting restClientTesting;
    @Autowired private CountryRepository countryRepository;
    @Autowired private PublisherRepository publisherRepository;
    private final Country argentina = new Country(0, "argentina", "argentinian");
    private final Country brazil = new Country(0, "brazil", "brazilian");
    private final Country chile = new Country(0, "chile", "chilean");
    private final Publisher publisher1 = new Publisher(0, "publisher1", "history1", brazil);
    private final Publisher publisher2 = new Publisher(0, "publisher2", "history2", brazil);
    private int createdCountryId = 0;
    private final String basePath = "countries";

    @BeforeAll
    @Transactional
    public void init() {

        countryRepository.save(argentina);
        countryRepository.save(brazil);
        countryRepository.save(chile);
        publisherRepository.save(publisher1);
        publisherRepository.save(publisher2);
    }

    @Test
    @Order(1)
    public void should_create_a_country_successfully() throws NoSuchMethodException {

        var usa = new CountryDto("usa_a", "usa_a");
        var response = restClientTesting.post(CountryDto.class, basePath, usa);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        var country = response.getBody();
        createdCountryId = (int) country.getId();
        assertThat(country.getId()).isInstanceOfAny(Integer.class);
        assertThat(country.getName()).isEqualTo("usa_a");
        assertThat(country.getGentilic()).isEqualTo("usa_a");
    }

    @Test
    @Order(2)
    public void should_update_created_country_successfully() throws NoSuchMethodException {

        var usa = new CountryDto("USA", "American");
        var response = restClientTesting.put(CountryDto.class, basePath + "/" + createdCountryId, usa);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var country = response.getBody();
        assertThat((int)country.getId()).isEqualTo(createdCountryId);
        assertThat(country.getName()).isEqualTo("USA");
        assertThat(country.getGentilic()).isEqualTo("American");
    }

    @Test
    @Order(3)
    public void should_get_by_id_created_country_successfully() throws NoSuchMethodException {

        var response = restClientTesting.get(CountryDto.class, basePath + "/" + createdCountryId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var country = response.getBody();
        assertThat(country.getId()).isEqualTo(createdCountryId);
        assertThat(country.getName()).isEqualTo("USA");
        assertThat(country.getGentilic()).isEqualTo("American");
    }

    @Test
    @Order(4)
    public void should_get_by_id_created_country_from_cache_successfully() throws NoSuchMethodException {

        var response = restClientTesting.get(CountryDto.class, basePath + "/" + createdCountryId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var country = response.getBody();
        assertThat(country.getId()).isEqualTo(createdCountryId);
        assertThat(country.getName()).isEqualTo("USA");
        assertThat(country.getGentilic()).isEqualTo("American");
    }

    @Test
    @Order(5)
    public void should_remove_created_country_successfully() {

        var url = String.format("%s/%s", basePath, createdCountryId);
        var response = restClientTesting.delete(url);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(6)
    public void should_get_all_countries_successfully() {

        var response = restClientTesting.get(CountryDto[].class, basePath);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var countries = response.getBody();
        assertThat(countries.length).isEqualTo(3);
    }

    @Test
    @Order(7)
    public void should_get_publishers_given_a_country_successfully() {

        var newPath = MessageFormat
                .format("{0}/{1}/{2}", basePath, brazil.getId(), "publishers");
        var response = restClientTesting.get(CountryDto[].class, newPath);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var publishers = response.getBody();
        assertThat(publishers.length).isEqualTo(2);
    }

    @Test
    @Order(8)
    public void should_not_remove_country_given_not_existent_id() {

        var url = String.format("%s/%s", basePath, 99);
        var response = restClientTesting.delete(url);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
    }

    @AfterAll
    @Transactional
    public void tearDown() {

        publisherRepository.remove(publisher1);
        publisherRepository.remove(publisher2);
        countryRepository.remove(argentina);
        countryRepository.remove(brazil);
        countryRepository.remove(chile);
    }
}
