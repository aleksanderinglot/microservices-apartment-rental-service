package com.example.apartmentrentalservice.service;

import com.example.apartmentrentalservice.dto.ApartmentDTO;
import com.example.apartmentrentalservice.exception.ApartmentNotFoundException;
import com.example.apartmentrentalservice.model.Apartment;
import com.example.apartmentrentalservice.repository.ApartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;

    public ApartmentService(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    public List<Apartment> getAllApartments() {
        return apartmentRepository.findAll();
    }

    public Apartment getApartmentById(Long id) {
        return apartmentRepository.findById(id)
                .orElseThrow(() -> new ApartmentNotFoundException("Apartment id not found - " + id));
    }

    public Apartment createApartment(Apartment apartment) {
        return apartmentRepository.save(apartment);
    }

    public Apartment updateApartment(Long id, Apartment updatedApartment) {
        Apartment apartment = getApartmentById(id);
        apartment.setName(updatedApartment.getName());
        apartment.setAddress(updatedApartment.getAddress());

        return apartmentRepository.save(apartment);
    }

    public void deleteApartment(Long id) {
        apartmentRepository.deleteById(id);
    }

    public ApartmentDTO convertToDTO(Apartment apartment) {
        return ApartmentDTO.builder()
                .id(apartment.getId())
                .name(apartment.getName())
                .address(apartment.getAddress())
                .numberOfRooms(apartment.getNumberOfRooms())
                .price(apartment.getPrice())
                .build();
    }

    public Apartment convertToEntity(ApartmentDTO apartmentDTO) {
        return Apartment.builder()
                .name(apartmentDTO.getName())
                .address(apartmentDTO.getAddress())
                .numberOfRooms(apartmentDTO.getNumberOfRooms())
                .price(apartmentDTO.getPrice())
                .build();
    }

    public List<ApartmentDTO> convertToDTOList(List<Apartment> apartments) {
        return apartments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
