package com.cokung.comon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableCaching
@EnableJpaAuditing
//@EntityScan("com.cokung.comon.domain.entity")
@SpringBootApplication
public class ComonApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComonApplication.class, args);
	}
}
