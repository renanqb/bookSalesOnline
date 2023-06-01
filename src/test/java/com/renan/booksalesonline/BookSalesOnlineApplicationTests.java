package com.renan.booksalesonline;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
class BookSalesOnlineApplicationTests {

	@ClassRule
	public static PostgreSQLContainer postgreSQLContainer
			= BookSalesOnlineDatabaseContainer.getInstance();

	@Test
	void contextLoads() {
	}
}
