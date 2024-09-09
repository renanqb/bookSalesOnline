package com.renan.booksalesonline.tests.testhelpers;

import org.junit.ClassRule;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class BookSalesOnlineContainerTest {

    @ClassRule
    public static PostgreSQLContainer<BookSalesOnlineDatabaseContainer> postgreSQLContainer =
            BookSalesOnlineDatabaseContainer.getInstance();

    @ClassRule
    public static GenericContainer<BookSalesOnlineRedisContainer> redisContainer =
            BookSalesOnlineRedisContainer.getInstance();
}
