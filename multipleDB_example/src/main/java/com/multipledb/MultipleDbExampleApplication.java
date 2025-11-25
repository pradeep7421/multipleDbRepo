package com.multipledb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MultipleDbExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultipleDbExampleApplication.class, args);
	}
	

}
