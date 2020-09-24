package com.example.springdatarestcrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringDataRestCrudApplication {

	/*
		This service uses an H2 database instance.

		Go to this page: https://www.baeldung.com/spring-boot-h2-database
		to setup the H2 console so you can view the database while the system is running.
	 */


	public static void main(String[] args) {
		SpringApplication.run(SpringDataRestCrudApplication.class, args);
	}

}
