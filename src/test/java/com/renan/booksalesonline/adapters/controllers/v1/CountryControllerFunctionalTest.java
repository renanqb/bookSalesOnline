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

    @Test
    @Order(1)
    public void should_create_a_country_successfully() throws NoSuchMethodException {

        var mexicoA = new CountryDto("mexico_a", "mexican_a");
        var countryResponse = countryController.create(mexicoA);

        assertThat(countryResponse.getId()).isEqualTo(4);
        assertThat(countryResponse.getName()).isEqualTo("mexico_a");
        assertThat(countryResponse.getGentilic()).isEqualTo("mexican_a");
    }

    @Test
    @Order(2)
    public void should_update_created_country_successfully() throws NoSuchMethodException {

        var mexicoB = new CountryDto("mexico", "mexican");
        var countryResponse = countryController.update(4, mexicoB);

        assertThat(countryResponse.getId()).isEqualTo(4);
        assertThat(countryResponse.getName()).isEqualTo("mexico");
        assertThat(countryResponse.getGentilic()).isEqualTo("mexican");
    }

    @Test
    @Order(3)
    public void should_get_by_id_created_country_successfully() throws NoSuchMethodException {

        var countryResponse = countryController.getCountryById(4);

        assertThat(countryResponse.getId()).isEqualTo(4);
        assertThat(countryResponse.getName()).isEqualTo("mexico");
        assertThat(countryResponse.getGentilic()).isEqualTo("mexican");
    }

    @Test
    @Order(4)
    public void should_remove_created_country_successfully() throws NoSuchMethodException {

        assertDoesNotThrow(() -> {
            countryController.remove(4);
        });
    }

    @Test
    @Order(5)
    public void should_get_all_countries_successfully() throws NoSuchMethodException {

        var countryResponse = countryController.getAllCountries();

        assertThat(countryResponse.size()).isEqualTo(3);
    }

    @BeforeAll
    @Transactional
    public void init() {

        countryRepository.save(argentina);
        countryRepository.save(brazil);
        countryRepository.save(chile);
    }
    @AfterAll
    public void tearDown() {

        countryRepository.remove(argentina);
        countryRepository.remove(brazil);
        countryRepository.remove(chile);
    }
}
