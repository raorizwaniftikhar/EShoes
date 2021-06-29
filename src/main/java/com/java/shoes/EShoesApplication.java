package com.java.shoes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EShoesApplication {

	public static void main(String[] args) {
		SpringApplication.run(EShoesApplication.class, args);
	}

}
