package com.example.apartmentrentalservice.controller;

import com.example.apartmentrentalservice.dto.ApartmentDTO;
import com.example.apartmentrentalservice.model.Apartment;
import com.example.apartmentrentalservice.service.ApartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ApartmentControllerTest {

    @Mock
    private ApartmentService apartmentService;

    @InjectMocks
    private ApartmentController apartmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllApartments() {

        List<Apartment> apartments = new ArrayList<>();
        apartments.add(new Apartment());
        when(apartmentService.getAllApartments()).thenReturn(apartments);

        List<ApartmentDTO> apartmentDTOs = new ArrayList<>();
        apartmentDTOs.add(new ApartmentDTO());
        when(apartmentService.convertToDTOList(apartments)).thenReturn(apartmentDTOs);

        ResponseEntity<List<ApartmentDTO>> response = apartmentController.getAllApartments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    void shouldReturnApartmentById() {

        Apartment apartment = new Apartment();
        apartment.setId(1L);
        when(apartmentService.getApartmentById(anyLong())).thenReturn(apartment);

        ApartmentDTO apartmentDTO = new ApartmentDTO();
        apartmentDTO.setId(1L);

        when(apartmentService.convertToDTO(apartment)).thenReturn(apartmentDTO);

        ResponseEntity<ApartmentDTO> response = apartmentController.getApartmentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldReturnNotFoundForNonExistentApartmentId() {
        when(apartmentService.getApartmentById(anyLong())).thenReturn(null);

        ResponseEntity<ApartmentDTO> response = apartmentController.getApartmentById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void shouldCreateApartment() {
        ApartmentDTO apartmentDTO = new ApartmentDTO();
        apartmentDTO.setId(1L);
        Apartment apartment = new Apartment();
        apartment.setId(1L);
        when(apartmentService.convertToEntity(apartmentDTO)).thenReturn(apartment);
        when(apartmentService.createApartment(apartment)).thenReturn(apartment);
        when(apartmentService.convertToDTO(apartment)).thenReturn(apartmentDTO);

        ResponseEntity<ApartmentDTO> response = apartmentController.createApartment(apartmentDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldUpdateApartment() {
        Long apartmentId = 1L;
        ApartmentDTO apartmentDTO = new ApartmentDTO();
        apartmentDTO.setId(apartmentId);
        Apartment existingApartment = new Apartment();
        existingApartment.setId(apartmentId);
        Apartment updatedApartment = new Apartment();
        updatedApartment.setId(apartmentId);
        when(apartmentService.getApartmentById(apartmentId)).thenReturn(existingApartment);
        when(apartmentService.convertToEntity(apartmentDTO)).thenReturn(updatedApartment);
        when(apartmentService.updateApartment(eq(apartmentId), any(Apartment.class))).thenReturn(updatedApartment);
        when(apartmentService.convertToDTO(updatedApartment)).thenReturn(apartmentDTO);

        ResponseEntity<ApartmentDTO> response = apartmentController.updateApartment(apartmentId, apartmentDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(apartmentId, response.getBody().getId());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentApartment() {
        Long apartmentId = 1L;
        when(apartmentService.getApartmentById(apartmentId)).thenReturn(null);

        ResponseEntity<ApartmentDTO> response = apartmentController.updateApartment(apartmentId, new ApartmentDTO());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void shouldDeleteApartment() {
        Long apartmentId = 1L;
        Apartment existingApartment = new Apartment();
        existingApartment.setId(apartmentId);
        when(apartmentService.getApartmentById(apartmentId)).thenReturn(existingApartment);

        ResponseEntity<Void> response = apartmentController.deleteApartment(apartmentId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(apartmentService, times(1)).deleteApartment(apartmentId);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentApartment() {
        Long apartmentId = 1L;
        when(apartmentService.getApartmentById(apartmentId)).thenReturn(null);

        ResponseEntity<Void> response = apartmentController.deleteApartment(apartmentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(apartmentService, never()).deleteApartment(apartmentId);
    }
}
