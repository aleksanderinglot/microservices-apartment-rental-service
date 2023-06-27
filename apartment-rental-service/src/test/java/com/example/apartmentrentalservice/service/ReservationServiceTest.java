package com.example.apartmentrentalservice.service;

import com.example.apartmentrentalservice.exception.ReservationNotFoundException;
import com.example.apartmentrentalservice.model.Reservation;
import com.example.apartmentrentalservice.repository.ApartmentRepository;
import com.example.apartmentrentalservice.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ApartmentRepository apartmentRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation());
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.getAllReservations();

        assertEquals(reservations.size(), result.size());
    }

    @Test
    void shouldReturnReservationById() {
        Long reservationId = 1L;
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.getReservationById(reservationId);

        assertNotNull(result);
        assertEquals(reservationId, result.getId());
    }

    @Test
    void shouldThrowExceptionWhenReservationNotFoundById() {
        Long reservationId = 1L;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> reservationService.getReservationById(reservationId));
    }

    @Test
    void shouldCreateReservation() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(1));
        reservation.setEndDate(LocalDate.now().plusDays(2));
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.createReservation(reservation);

        assertNotNull(result);
    }

    @Test
    void shouldThrowExceptionWhenDateRangeInvalid() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(1));
        reservation.setEndDate(LocalDate.now());

        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(reservation));
    }

    @Test
    void shouldUpdateReservation() {
        Long reservationId = 1L;
        Reservation existingReservation = new Reservation();
        existingReservation.setId(reservationId);
        existingReservation.setStartDate(LocalDate.now());
        existingReservation.setEndDate(LocalDate.now().plusDays(3));
        Reservation updatedReservation = new Reservation();
        updatedReservation.setId(reservationId);
        updatedReservation.setStartDate(LocalDate.now().plusDays(1));
        updatedReservation.setEndDate(LocalDate.now().plusDays(2));
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(existingReservation));
        when(reservationRepository.save(updatedReservation)).thenReturn(updatedReservation);
        Reservation result = reservationService.updateReservation(reservationId, updatedReservation);

        assertNotNull(result);
        assertEquals(reservationId, result.getId());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentReservation() {
        Long reservationId = 1L;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> reservationService.updateReservation(reservationId, new Reservation()));
    }

    @Test
    void shouldDeleteReservation() {
        Long reservationId = 1L;

        reservationService.deleteReservation(reservationId);

        verify(reservationRepository, times(1)).deleteById(reservationId);
    }

    @Test
    void shouldNotDetectOverlapWhenNoneExists() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().minusDays(2));
        reservation.setEndDate(LocalDate.now().minusDays(1));
        when(reservationRepository.findByApartmentId(anyLong())).thenReturn(new ArrayList<>());

        boolean result = reservationService.checkReservationOverlap(reservation);

        assertFalse(result);
    }

    @Test
    void shouldDetectOverlap() {
        Reservation reservation = new Reservation();
        reservation.setApartmentId(1L);
        reservation.setStartDate(LocalDate.now().minusDays(2));
        reservation.setEndDate(LocalDate.now().plusDays(2));
        Reservation existingReservation = new Reservation();
        existingReservation.setApartmentId(1L);
        existingReservation.setStartDate(LocalDate.now().minusDays(1));
        existingReservation.setEndDate(LocalDate.now().plusDays(1));
        List<Reservation> existingReservations = new ArrayList<>();
        existingReservations.add(existingReservation);
        when(reservationRepository.findByApartmentId(1L)).thenReturn(existingReservations);

        boolean result = reservationService.checkReservationOverlap(reservation);

        assertTrue(result);
    }

    @Test
    void shouldNotDetectOverlapForUpdateWhenNoneExists() {
        Reservation reservationToUpdate = new Reservation();
        reservationToUpdate.setId(2L);
        reservationToUpdate.setApartmentId(1L);
        reservationToUpdate.setStartDate(LocalDate.now().minusDays(2));
        reservationToUpdate.setEndDate(LocalDate.now().minusDays(1));
        Reservation existingReservation = new Reservation();
        existingReservation.setId(1L);
        existingReservation.setApartmentId(1L);
        existingReservation.setStartDate(LocalDate.now().plusDays(1));
        existingReservation.setEndDate(LocalDate.now().plusDays(2));
        List<Reservation> existingReservations = new ArrayList<>();
        existingReservations.add(existingReservation);
        when(reservationRepository.findByApartmentIdAndIdNot(1L, 2L)).thenReturn(existingReservations);

        boolean result = reservationService.checkReservationOverlapForUpdate(reservationToUpdate);

        assertFalse(result);
    }

    @Test
    void shouldDetectOverlapForUpdate() {
        Reservation reservationToUpdate = new Reservation();
        reservationToUpdate.setId(2L);
        reservationToUpdate.setApartmentId(1L);
        reservationToUpdate.setStartDate(LocalDate.now().minusDays(2));
        reservationToUpdate.setEndDate(LocalDate.now().plusDays(2));
        Reservation existingReservation = new Reservation();
        existingReservation.setId(1L);
        existingReservation.setApartmentId(1L);
        existingReservation.setStartDate(LocalDate.now().minusDays(1));
        existingReservation.setEndDate(LocalDate.now().plusDays(1));
        List<Reservation> existingReservations = new ArrayList<>();
        existingReservations.add(existingReservation);
        when(reservationRepository.findByApartmentIdAndIdNot(eq(1L), eq(2L))).thenReturn(existingReservations);

        boolean result = reservationService.checkReservationOverlapForUpdate(reservationToUpdate);

        assertTrue(result);
    }
}
