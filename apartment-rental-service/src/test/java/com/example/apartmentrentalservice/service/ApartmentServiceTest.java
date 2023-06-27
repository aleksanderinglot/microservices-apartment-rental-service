package com.example.apartmentrentalservice.service;

import com.example.apartmentrentalservice.exception.ApartmentNotFoundException;
import com.example.apartmentrentalservice.model.Apartment;
import com.example.apartmentrentalservice.repository.ApartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApartmentServiceTest {

    @Mock
    private ApartmentRepository apartmentRepository;

    @InjectMocks
    private ApartmentService apartmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllApartments() {
        List<Apartment> apartments = new ArrayList<>();
        apartments.add(new Apartment());
        when(apartmentRepository.findAll()).thenReturn(apartments);

        List<Apartment> result = apartmentService.getAllApartments();

        assertEquals(apartments.size(), result.size());
    }

    @Test
    void shouldGetApartmentById() {
        Long apartmentId = 1L;
        Apartment apartment = new Apartment();
        apartment.setId(apartmentId);
        when(apartmentRepository.findById(apartmentId)).thenReturn(Optional.of(apartment));

        Apartment result = apartmentService.getApartmentById(apartmentId);

        assertNotNull(result);
        assertEquals(apartmentId, result.getId());
    }

    @Test
    void shouldNotFindApartmentById() {
        Long apartmentId = 1L;
        when(apartmentRepository.findById(apartmentId)).thenReturn(Optional.empty());

        assertThrows(ApartmentNotFoundException.class, () -> apartmentService.getApartmentById(apartmentId));
    }

    @Test
    void shouldCreateApartment() {
        Apartment apartment = new Apartment();
        when(apartmentRepository.save(apartment)).thenReturn(apartment);

        Apartment result = apartmentService.createApartment(apartment);

        assertNotNull(result);
    }

    @Test
    void shouldUpdateApartment() {
        Long apartmentId = 1L;
        Apartment existingApartment = new Apartment();
        existingApartment.setId(apartmentId);
        Apartment updatedApartment = new Apartment();
        updatedApartment.setId(apartmentId);
        when(apartmentRepository.findById(apartmentId)).thenReturn(Optional.of(existingApartment));
        when(apartmentRepository.save(updatedApartment)).thenReturn(updatedApartment);

        Apartment result = apartmentService.updateApartment(apartmentId, updatedApartment);

        assertNotNull(result);
        assertEquals(apartmentId, result.getId());
    }

    @Test
    void shouldNotUpdateNonExistingApartment() {
        Long apartmentId = 1L;
        when(apartmentRepository.findById(apartmentId)).thenReturn(Optional.empty());

        assertThrows(ApartmentNotFoundException.class, () ->
                apartmentService.updateApartment(apartmentId, new Apartment()));
    }

    @Test
    void shouldDeleteApartment() {
        Long apartmentId = 1L;

        apartmentService.deleteApartment(apartmentId);

        verify(apartmentRepository, times(1)).deleteById(apartmentId);
    }
}
