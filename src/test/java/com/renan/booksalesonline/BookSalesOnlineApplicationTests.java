package com.renan.booksalesonline;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
class BookSalesOnlineApplicationTests {

	@ClassRule
	public static PostgreSQLContainer postgreSQLContainer
			= BookSalesOnlineDatabaseContainer.getInstance();

	@Test
	void contextLoads() {
	}

}
