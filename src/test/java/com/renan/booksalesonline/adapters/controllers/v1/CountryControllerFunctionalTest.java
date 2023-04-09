package com.renan.booksalesonline.adapters.controllers.v1;

import com.renan.booksalesonline.adapters.controllers.v1.model.CountryDto;
import com.renan.booksalesonline.adapters.repositories.CountryRepository;
import com.renan.booksalesonline.domain.Country;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CountryControllerFunctionalTest {

    @Autowired private CountryRepository countryRepository;
    @Autowired private CountryController countryController;

    private Country argentina = new Country(0, "argentina", "argentinian");
    private Country brazil = new Country(0, "brazil", "brazilian");
    private Country chile = new Country(0, "chile", "chilean");
    private int createdCountryId = 0;

    @BeforeAll
    @Transactional
    public void init() {

        countryRepository.save(argentina);
        countryRepository.save(brazil);
        countryRepository.save(chile);
    }

    @Test
    @Order(1)
    public void should_create_a_country_successfully() throws NoSuchMethodException {

        var usa = new CountryDto("usa_a", "usa_a");
        var countryResponse = countryController.create(usa);
        createdCountryId = (int) countryResponse.getId();

        assertThat(countryResponse.getId()).isInstanceOfAny(Integer.class);
        assertThat(countryResponse.getName()).isEqualTo("usa_a");
        assertThat(countryResponse.getGentilic()).isEqualTo("usa_a");
    }

    @Test
    @Order(2)
    public void should_update_created_country_successfully() throws NoSuchMethodException {

        var usa = new CountryDto("USA", "American");
        var countryResponse = countryController.update(createdCountryId, usa);

        assertThat(countryResponse.getId()).isEqualTo(createdCountryId);
        assertThat(countryResponse.getName()).isEqualTo("USA");
        assertThat(countryResponse.getGentilic()).isEqualTo("American");
    }

    @Test
    @Order(3)
    public void should_get_by_id_created_country_successfully() throws NoSuchMethodException {

        var countryResponse = countryController.getCountryById(createdCountryId);

        assertThat(countryResponse.getId()).isEqualTo(createdCountryId);
        assertThat(countryResponse.getName()).isEqualTo("USA");
        assertThat(countryResponse.getGentilic()).isEqualTo("American");
    }

    @Test
    @Order(4)
    public void should_remove_created_country_successfully() throws NoSuchMethodException {

        assertDoesNotThrow(() -> {
            countryController.remove(createdCountryId);
        });
    }

    @Test
    @Order(5)
    public void should_get_all_countries_successfully() throws NoSuchMethodException {

        var countryResponse = countryController.getAllCountries();

        assertThat(countryResponse.size()).isEqualTo(3);
    }


    @AfterAll
    @Transactional
    public void tearDown() {

        countryRepository.remove(argentina);
        countryRepository.remove(brazil);
        countryRepository.remove(chile);
    }
}
