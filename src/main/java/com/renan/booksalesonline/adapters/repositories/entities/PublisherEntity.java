package com.renan.booksalesonline.adapters.repositories.entities;

import javax.persistence.*;

@Entity
@Table(name = "publisher")
public class PublisherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "history", nullable = false)
    private String history;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_country", referencedColumnName = "id")
    private CountryEntity country;

    protected PublisherEntity() {
        this(0, "", "", null);
    }

    public PublisherEntity(int id, String name, String history, CountryEntity country) {
        setId(id);
        setName(name);
        setHistory(history);
        setCountry(country);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
    }
}
