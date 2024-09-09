package com.renan.booksalesonline.adapters.repositories;

import com.renan.booksalesonline.adapters.repositories.data.LanguageData;
import com.renan.booksalesonline.adapters.repositories.mappers.LanguageEntityMapper;
import com.renan.booksalesonline.application.ports.out.DataCommand;
import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.domain.Language;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class LanguageRepository implements DataQuery<Language>, DataCommand<Language> {

    private final LanguageData languageData;

    @Override
    public List<Language> getAll() {

        var languageEntities = languageData.findAll();
        return LanguageEntityMapper.toDomain(languageEntities);
    }

    @Override
    public Language getById(int id) {

        var optCountryEntity = languageData.findById(id);
        return optCountryEntity
                .map(LanguageEntityMapper::toDomain)
                .orElse(null);
    }

    @Override
    public Language save(Language language) {

        var languageEntity = LanguageEntityMapper.fromDomain(language);
        languageData.save(languageEntity);
        language.setId(languageEntity.getId());
        return language;
    }

    @Override
    public void remove(Language language) {

        var languageEntity = LanguageEntityMapper.fromDomain(language);
        languageData.delete(languageEntity);
    }
}
