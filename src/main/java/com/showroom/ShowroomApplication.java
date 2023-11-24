package com.showroom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ShowroomApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShowroomApplication.class, args);
		log.info("Starting spring.....");
	}

}
