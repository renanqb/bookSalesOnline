package com.renan.booksalesonline.testhelpers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class BookSalesOnlineRedisContainer extends GenericContainer<BookSalesOnlineRedisContainer> {

    private static final String IMAGE_VERSION = "redis:7.0.11";
    private static BookSalesOnlineRedisContainer container;

    private BookSalesOnlineRedisContainer() {
        super(DockerImageName.parse(IMAGE_VERSION));
    }

    public static BookSalesOnlineRedisContainer getInstance() {

        if (container == null) {
            container = new BookSalesOnlineRedisContainer().withExposedPorts(6379);
            container.start();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("spring.redis.host", container.getHost());
        System.setProperty("spring.redis.port", container.getMappedPort(6379).toString());
    }
}
