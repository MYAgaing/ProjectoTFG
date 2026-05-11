package com.reviewmeter.tfg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ReviewmeterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewmeterApplication.class, args);
	}

}
