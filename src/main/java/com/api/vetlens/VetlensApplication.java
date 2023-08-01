package com.api.vetlens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VetlensApplication {
	public static void main(String[] args) {
		SpringApplication.run(VetlensApplication.class, args);
	}

}
