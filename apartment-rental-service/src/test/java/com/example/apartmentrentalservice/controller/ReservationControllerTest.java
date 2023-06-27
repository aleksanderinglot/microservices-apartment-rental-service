package com.example.apartmentrentalservice.controller;

import com.example.apartmentrentalservice.dto.ReservationDTO;
import com.example.apartmentrentalservice.model.Reservation;
import com.example.apartmentrentalservice.service.ReservationService;
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

class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation());
        when(reservationService.getAllReservations()).thenReturn(reservations);

        List<ReservationDTO> reservationDTOs = new ArrayList<>();
        reservationDTOs.add(new ReservationDTO());
        when(reservationService.convertToDTOList(reservations)).thenReturn(reservationDTOs);

        ResponseEntity<List<ReservationDTO>> response = reservationController.getAllReservations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    void shouldGetReservationById() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        when(reservationService.getReservationById(anyLong())).thenReturn(reservation);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(1L);
        when(reservationService.convertToDTO(reservation)).thenReturn(reservationDTO);

        ResponseEntity<ReservationDTO> response = reservationController.getReservationById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldNotFindReservationById() {
        when(reservationService.getReservationById(anyLong())).thenReturn(null);

        ResponseEntity<ReservationDTO> response = reservationController.getReservationById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void shouldCreateReservation() {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(1L);
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        when(reservationService.convertToEntity(reservationDTO)).thenReturn(reservation);
        when(reservationService.createReservation(reservation)).thenReturn(reservation);
        when(reservationService.convertToDTO(reservation)).thenReturn(reservationDTO);

        ResponseEntity<ReservationDTO> response = reservationController.createReservation(reservationDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldUpdateReservation() {
        Long reservationId = 1L;
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(reservationId);
        Reservation existingReservation = new Reservation();
        existingReservation.setId(reservationId);
        Reservation updatedReservation = new Reservation();
        updatedReservation.setId(reservationId);
        when(reservationService.getReservationById(reservationId)).thenReturn(existingReservation);
        when(reservationService.convertToEntity(reservationDTO)).thenReturn(updatedReservation);
        when(reservationService.updateReservation(eq(reservationId), any(Reservation.class))).thenReturn(updatedReservation);
        when(reservationService.convertToDTO(updatedReservation)).thenReturn(reservationDTO);

        ResponseEntity<ReservationDTO> response = reservationController.updateReservation(reservationId, reservationDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(reservationId, response.getBody().getId());
    }

    @Test
    void shouldNotUpdateNonExistingReservation() {
        Long reservationId = 1L;
        when(reservationService.getReservationById(reservationId)).thenReturn(null);

        ResponseEntity<ReservationDTO> response = reservationController.updateReservation(reservationId, new ReservationDTO());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void shouldDeleteReservation() {
        Long reservationId = 1L;
        Reservation existingReservation = new Reservation();
        existingReservation.setId(reservationId);
        when(reservationService.getReservationById(reservationId)).thenReturn(existingReservation);

        ResponseEntity<Void> response = reservationController.deleteReservation(reservationId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(reservationService, times(1)).deleteReservation(reservationId);
    }

    @Test
    void shouldNotDeleteNonExistingReservation() {
        Long reservationId = 1L;
        when(reservationService.getReservationById(reservationId)).thenReturn(null);

        ResponseEntity<Void> response = reservationController.deleteReservation(reservationId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(reservationService, never()).deleteReservation(reservationId);
    }
}
