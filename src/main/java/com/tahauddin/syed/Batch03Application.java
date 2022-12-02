package com.tahauddin.syed;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class Batch03Application {

	public static void main(String[] args) {
		SpringApplication.run(Batch03Application.class, args);
	}

}
