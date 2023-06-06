package com.renan.booksalesonline.testhelpers;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class BookSalesOnlineDatabaseContainer extends PostgreSQLContainer<BookSalesOnlineDatabaseContainer> {

    private static final String IMAGE_VERSION = "postgres:15.2";
    private static BookSalesOnlineDatabaseContainer container;

    private BookSalesOnlineDatabaseContainer() {
        super(DockerImageName.parse(IMAGE_VERSION));
    }

    public static BookSalesOnlineDatabaseContainer getInstance() {
        if (container == null) {
            container = new BookSalesOnlineDatabaseContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }
}
