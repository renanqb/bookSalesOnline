package com.renan.booksalesonline.tests.adapters.controllers.v1;

import com.renan.booksalesonline.adapters.controllers.v1.model.SubjectDto;
import com.renan.booksalesonline.adapters.repositories.SubjectRepository;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SubjectControllerFunctionalTest extends BookSalesOnlineContainerTest {

    @Autowired private RestClientTesting restClientTesting;
    @Autowired private SubjectRepository subjectRepository;

    private final String basePath = "subjects";
    private int createdSubjectId = 0;

    @BeforeAll
    @Transactional
    public void init() {

        subjectRepository.getAll(0, 100); // Ensure repository is initialized
    }

    @Test
    @Order(1)
    public void should_create_subject_successfully() {

        var newSubject = new SubjectDto("Ficção Científica", "Livros sobre ficção científica");
        var response = restClientTesting.post(SubjectDto.class, basePath, newSubject);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        var subject = response.getBody();
        assert subject != null;
        assertThat((int) subject.getId()).isGreaterThanOrEqualTo(1);
        assertThat(subject.getName()).isEqualTo("Ficção Científica");
        assertThat(subject.getDescription()).isEqualTo("Livros sobre ficção científica");

        createdSubjectId = (int) subject.getId();
    }

    @Test
    @Order(2)
    public void should_update_created_subject_successfully() {

        var updatedSubject = new SubjectDto("Ficção Científica Atualizado", "Livros atualizados");
        var response = restClientTesting.put(SubjectDto.class, basePath + "/" + createdSubjectId, updatedSubject);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var subject = response.getBody();
        assert subject != null;
        assertThat((int) subject.getId()).isEqualTo(createdSubjectId);
        assertThat(subject.getName()).isEqualTo("Ficção Científica Atualizado");
        assertThat(subject.getDescription()).isEqualTo("Livros atualizados");
    }

    @Test
    @Order(3)
    public void should_get_by_id_created_subject_successfully() {

        var response = restClientTesting.get(SubjectDto.class, basePath + "/" + createdSubjectId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var subject = response.getBody();
        assert subject != null;
        assertThat((int) subject.getId()).isEqualTo(createdSubjectId);
        assertThat(subject.getName()).isEqualTo("Ficção Científica Atualizado");
    }

    @Test
    @Order(4)
    public void should_get_by_id_created_subject_from_cache_successfully() {

        var response = restClientTesting.get(SubjectDto.class, basePath + "/" + createdSubjectId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var subject = response.getBody();
        assert subject != null;
        assertThat((int) subject.getId()).isEqualTo(createdSubjectId);
    }

    @Test
    @Order(5)
    public void should_delete_created_subject_successfully() {

        var response = restClientTesting.delete(basePath + "/" + createdSubjectId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @AfterAll
    @Transactional
    public void tearDown() {
        // Cleanup if needed
    }
}



