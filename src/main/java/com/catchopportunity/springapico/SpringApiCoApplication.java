package com.catchopportunity.springapico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "http://localhost:8081")
public class SpringApiCoApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(SpringApiCoApplication.class, args);
	}
}
