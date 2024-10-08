package com.renan.booksalesonline.tests.adapters.controllers.v1;

import com.renan.booksalesonline.adapters.controllers.v1.LanguageController;
import com.renan.booksalesonline.adapters.controllers.v1.model.LanguageDto;
import com.renan.booksalesonline.adapters.repositories.LanguageRepository;
import com.renan.booksalesonline.domain.Language;
import com.renan.booksalesonline.tests.testhelpers.BookSalesOnlineContainerTest;
import com.renan.booksalesonline.tests.testhelpers.RestClientTesting;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LanguageControllerFunctionalTest extends BookSalesOnlineContainerTest {

    @Autowired private RestClientTesting restClientTesting;
    @Autowired private LanguageRepository languageRepository;
    @Autowired private LanguageController languageController;
    private final Language english = new Language(0, "english");
    private final Language portuguese = new Language(0, "portuguese");
    private final Language chinese = new Language(0, "chinese");
    private int createdLanguageId = 0;
    private final String basePath = "languages";

    @BeforeAll
    @Transactional
    public void init() {

        languageRepository.save(english);
        languageRepository.save(portuguese);
        languageRepository.save(chinese);
    }

    @Test
    @Order(1)
    public void should_create_a_language_successfully() throws NoSuchMethodException {

        var spanish = new LanguageDto("spanish_a");
        var response = restClientTesting.post(LanguageDto.class, basePath, spanish);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        var language = response.getBody();
        assert language != null;
        assertThat(language.getId()).isInstanceOfAny(Integer.class);
        assertThat(language.getName()).isEqualTo("spanish_a");

        createdLanguageId = (int) language.getId();
    }

    @Test
    @Order(2)
    public void should_update_created_language_successfully() throws NoSuchMethodException {

        var spanish = new LanguageDto("spanish");
        var response = restClientTesting.put(LanguageDto.class, basePath + "/" + createdLanguageId, spanish);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var language = response.getBody();
        assert language != null;
        assertThat((int)language.getId()).isEqualTo(createdLanguageId);
        assertThat(language.getName()).isEqualTo("spanish");
    }

    @Test
    @Order(3)
    public void should_get_by_id_created_language_successfully() throws NoSuchMethodException {

        var response = restClientTesting.get(LanguageDto.class, basePath + "/" + createdLanguageId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var language = response.getBody();
        assert language != null;
        assertThat(language.getId()).isEqualTo(createdLanguageId);
        assertThat(language.getName()).isEqualTo("spanish");
    }

    @Test
    @Order(4)
    public void should_remove_created_language_successfully() {

        var url = String.format("%s/%s", basePath, createdLanguageId);
        var response = restClientTesting.delete(url);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(5)
    public void should_get_all_languages_successfully() {

        var response = restClientTesting.get(LanguageDto[].class, basePath);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var countries = response.getBody();
        assert countries != null;
        assertThat(countries.length).isEqualTo(3);
    }

    @Test
    @Order(6)
    public void should_not_remove_language_given_not_existent_id() {

        var url = String.format("%s/%s", basePath, 99);
        var response = restClientTesting.delete(url);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
    }

    @AfterAll
    @Transactional
    public void tearDown() {

        languageRepository.remove(english);
        languageRepository.remove(portuguese);
        languageRepository.remove(chinese);
    }
}
