package com.shopme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ShopmeFrontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmeFrontEndApplication.class, args);
	}

}
