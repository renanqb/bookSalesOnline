package com.renan.booksalesonline.tests.adapters.controllers.v1.model;

import com.renan.booksalesonline.adapters.controllers.v1.model.SubjectDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SubjectDtoTest {

    @Test
    public void should_create_a_subject_dto_instance_successfully() {
        var expected = new SubjectDto("Fantasia", "Literatura de fantasia");

        assertThat(expected.getId()).isEqualTo(0);
        assertThat(expected.getName()).isEqualTo("Fantasia");
        assertThat(expected.getDescription()).isEqualTo("Literatura de fantasia");
    }

    @Test
    public void should_create_a_subject_dto_with_constructor_without_id() {
        var expected = new SubjectDto("Aventura", "Histórias de aventura");

        assertThat(expected.getName()).isEqualTo("Aventura");
        assertThat(expected.getDescription()).isEqualTo("Histórias de aventura");
    }

    @Test
    public void should_set_and_get_id() {
        var subjectDto = new SubjectDto("Policial", "");
        subjectDto.setId(5);

        assertThat(subjectDto.getId()).isEqualTo(5);
    }

    @Test
    public void should_set_and_get_name() {
        var subjectDto = new SubjectDto("Fantasia", "");
        subjectDto.setName("Romance");

        assertThat(subjectDto.getName()).isEqualTo("Romance");
    }

    @Test
    public void should_create_default_subject_dto() {
        var subjectDto = new SubjectDto();

        assertThat(subjectDto.getId()).isEqualTo(0);
        assertThat(subjectDto.getName()).isNull();
        assertThat(subjectDto.getDescription()).isNull();
    }

    @Test
    public void should_verify_equality_of_subject_dtos() {
        var dto1 = new SubjectDto("Fantasia", "");
        dto1.setId(1);
        var dto2 = new SubjectDto("Fantasia", "");
        dto2.setId(1);

        assertThat(dto1).isEqualTo(dto2);
    }

    @Test
    public void should_verify_inequality_of_subject_dtos_with_different_ids() {
        var dto1 = new SubjectDto("Fantasia", "");
        dto1.setId(1);
        var dto2 = new SubjectDto("Fantasia", "");
        dto2.setId(2);

        assertThat(dto1).isNotEqualTo(dto2);
    }
}


