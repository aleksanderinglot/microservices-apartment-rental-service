package com.example.apartmentrentalservice.config;

import com.example.apartmentrentalservice.model.Apartment;
import com.example.apartmentrentalservice.repository.ApartmentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializationConfig {

    private final ApartmentRepository apartmentRepository;

    public DataInitializationConfig(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    @PostConstruct
    public void initializeData() {
        Apartment apartment1 = new Apartment(1L, "Apartment 1", "Address 1", 2, 1000.00);
        apartmentRepository.save(apartment1);

        Apartment apartment2 = new Apartment(2L, "Apartment 2", "Address 2", 3, 1500.00);
        apartmentRepository.save(apartment2);

        Apartment apartment3 = new Apartment(3L, "Apartment 3", "Address 3", 4, 2000.00);
        apartmentRepository.save(apartment3);
    }
}
