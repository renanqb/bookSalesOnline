package com.renan.booksalesonline;

import org.testcontainers.containers.PostgreSQLContainer;

public class BookSalesOnlineDatabaseContainer extends PostgreSQLContainer<BookSalesOnlineDatabaseContainer> {

    private static final String IMAGE_VERSION = "postgres:15.2";
    private static BookSalesOnlineDatabaseContainer container;

    private BookSalesOnlineDatabaseContainer() {
        super(IMAGE_VERSION);
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

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
