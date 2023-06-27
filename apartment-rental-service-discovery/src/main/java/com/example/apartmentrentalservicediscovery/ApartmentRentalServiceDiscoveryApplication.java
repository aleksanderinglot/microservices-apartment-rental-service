package com.example.apartmentrentalservicediscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ApartmentRentalServiceDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApartmentRentalServiceDiscoveryApplication.class, args);
	}

}
