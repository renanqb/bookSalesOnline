package com.renan.booksalesonline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BookSalesOnlineApplication {

	public static void main(String[] args) {

		SpringApplication.run(BookSalesOnlineApplication.class, args);
	}
}
