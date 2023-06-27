package com.example.apartmentrentalservice.controller;

import com.example.apartmentrentalservice.dto.ApartmentDTO;
import com.example.apartmentrentalservice.model.Apartment;
import com.example.apartmentrentalservice.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apartments")
@CrossOrigin(origins = "*")
public class ApartmentController {

    private final ApartmentService apartmentService;

    @Autowired
    public ApartmentController(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    @GetMapping
    public ResponseEntity<List<ApartmentDTO>> getAllApartments() {
        List<Apartment> apartments = apartmentService.getAllApartments();

        if (!apartments.isEmpty()) {
            List<ApartmentDTO> apartmentDTOs = apartmentService.convertToDTOList(apartments);
            return ResponseEntity.ok(apartmentDTOs);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartmentDTO> getApartmentById(@PathVariable Long id) {
        Apartment apartment = apartmentService.getApartmentById(id);

        if (apartment != null) {
            ApartmentDTO apartmentDTO = apartmentService.convertToDTO(apartment);
            return ResponseEntity.ok(apartmentDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ApartmentDTO> createApartment(@RequestBody ApartmentDTO apartmentDTO) {
        Apartment apartment = apartmentService.convertToEntity(apartmentDTO);
        Apartment savedApartment = apartmentService.createApartment(apartment);

        ApartmentDTO savedApartmentDTO = apartmentService.convertToDTO(savedApartment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedApartmentDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApartmentDTO> updateApartment(@PathVariable Long id, @RequestBody ApartmentDTO apartmentDTO) {
        Apartment existingApartment = apartmentService.getApartmentById(id);

        if (existingApartment != null) {
            Apartment updatedApartment = apartmentService.convertToEntity(apartmentDTO);
            updatedApartment.setId(existingApartment.getId());

            Apartment savedApartment = apartmentService.updateApartment(id, updatedApartment);

            ApartmentDTO savedApartmentDTO = apartmentService.convertToDTO(savedApartment);
            return ResponseEntity.ok(savedApartmentDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable Long id) {
        Apartment existingApartment = apartmentService.getApartmentById(id);

        if (existingApartment != null) {
            apartmentService.deleteApartment(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
