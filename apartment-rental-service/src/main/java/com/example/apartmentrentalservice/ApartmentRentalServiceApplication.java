package com.example.apartmentrentalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApartmentRentalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApartmentRentalServiceApplication.class, args);
	}

}
