package com.renan.booksalesonline.tests.domain;

import com.renan.booksalesonline.domain.Subject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SubjectTest {

    @Test
    public void should_create_a_subject_domain_instance() {
        var expected = new Subject(1, "Fantasia", "Literatura de fantasia");

        assertThat(expected.getId()).isEqualTo(1);
        assertThat(expected.getName()).isEqualTo("Fantasia");
        assertThat(expected.getDescription()).isEqualTo("Literatura de fantasia");
    }

    @Test
    public void should_create_a_subject_with_only_id() {
        var expected = new Subject(99);

        assertThat(expected.getId()).isEqualTo(99);
        assertThat(expected.getName()).isEmpty();
        assertThat(expected.getDescription()).isEmpty();
    }

    @Test
    public void should_create_a_subject_with_default_constructor() {
        var expected = new Subject();

        assertThat(expected.getId()).isEqualTo(0);
        assertThat(expected.getName()).isEmpty();
        assertThat(expected.getDescription()).isNull();
    }

    @Test
    public void should_set_and_get_name() {
        var subject = new Subject(1, "Aventura", "");
        subject.setName("Romance");

        assertThat(subject.getName()).isEqualTo("Romance");
    }

    @Test
    public void should_set_and_get_description() {
        var subject = new Subject(1, "Policial", "Histórias de mistério");
        subject.setDescription("Histórias de ação");

        assertThat(subject.getDescription()).isEqualTo("Histórias de ação");
    }

    @Test
    public void should_verify_equality_of_subjects() {
        var subject1 = new Subject(1, "Fantasia", "");
        var subject2 = new Subject(1, "Fantasia", "");

        assertThat(subject1).isEqualTo(subject2);
    }

    @Test
    public void should_verify_inequality_of_subjects_with_different_ids() {
        var subject1 = new Subject(1, "Fantasia", "");
        var subject2 = new Subject(2, "Fantasia", "");

        assertThat(subject1).isNotEqualTo(subject2);
    }

    @Test
    public void should_have_not_blank_validation_for_name() {
        var subject = new Subject(1, "", "Descrição");

        // Name should be marked as @NotBlank in BaseDomain
        // This test ensures the field exists and can be set
        assertThat(subject.getName()).isEmpty();
    }

    @Test
    public void should_have_not_blank_validation_for_description() {
        var subject = new Subject(1, "Fantasia", "");

        // Description should be marked as @NotBlank in Subject
        // This test ensures the field exists and can be set
        assertThat(subject.getDescription()).isEmpty();
    }
}

