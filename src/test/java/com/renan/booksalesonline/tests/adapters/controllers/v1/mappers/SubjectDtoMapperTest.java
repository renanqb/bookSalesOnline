package com.renan.booksalesonline.tests.adapters.controllers.v1.mappers;

import com.renan.booksalesonline.adapters.controllers.v1.mappers.SubjectDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.model.SubjectDto;
import com.renan.booksalesonline.domain.Subject;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SubjectDtoMapperTest {

    @Test
    public void should_parse_subject_dto_to_subject_domain() {
        var dto = new SubjectDto("Fantasia", "Literatura de fantasia");
        dto.setId(1);

        var expected = new Subject(1, "Fantasia", "Literatura de fantasia");
        var actual = SubjectDtoMapper.toDomain(dto);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void should_parse_subject_domain_to_subject_dto() {
        var domain = new Subject(1, "Aventura", "Histórias de aventura e exploração");

        var actual = SubjectDtoMapper.fromDomain(domain);

        assertThat(actual.getId()).isEqualTo(1);
        assertThat(actual.getName()).isEqualTo("Aventura");
        assertThat(actual.getDescription()).isEqualTo("Histórias de aventura e exploração");
    }

    @Test
    public void should_parse_subject_domain_list_to_subject_dto_array() {
        var subjects = Arrays.asList(
                new Subject(1, "Fantasia", ""),
                new Subject(2, "Aventura", ""),
                new Subject(3, "Policial", "")
        );

        var actual = SubjectDtoMapper.fromDomain(subjects);

        assertThat(actual).hasSize(3);
        assertThat(actual[0].getName()).isEqualTo("Fantasia");
        assertThat(actual[1].getName()).isEqualTo("Aventura");
        assertThat(actual[2].getName()).isEqualTo("Policial");
    }
}

