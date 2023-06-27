package com.example.apartmentrentalservice.controller;

import com.example.apartmentrentalservice.dto.ReservationDTO;
import com.example.apartmentrentalservice.model.Reservation;
import com.example.apartmentrentalservice.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();

        if (!reservations.isEmpty()) {
            List<ReservationDTO> reservationDTOs = reservationService.convertToDTOList(reservations);
            return ResponseEntity.ok(reservationDTOs);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);

        if (reservation != null) {
            ReservationDTO reservationDTO = reservationService.convertToDTO(reservation);
            return ResponseEntity.ok(reservationDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<ReservationDTO>> getUserReservations(@PathVariable String username) {
        List<Reservation> reservations = reservationService.getUserReservations(username);

        if (!reservations.isEmpty()) {
            List<ReservationDTO> reservationDTOs = reservationService.convertToDTOList(reservations);
            return ResponseEntity.ok(reservationDTOs);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        Reservation reservation = reservationService.convertToEntity(reservationDTO);

        boolean isOverlap = reservationService.checkReservationOverlap(reservation);
        if (isOverlap) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        Reservation savedReservation = reservationService.createReservation(reservation);
        ReservationDTO savedReservationDTO = reservationService.convertToDTO(savedReservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReservationDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Long id, @RequestBody ReservationDTO reservationDTO) {
        Reservation existingReservation = reservationService.getReservationById(id);

        if (existingReservation != null) {
            Reservation reservationToUpdate = reservationService.convertToEntity(reservationDTO);
            reservationToUpdate.setId(existingReservation.getId());

            boolean isOverlap = reservationService.checkReservationOverlapForUpdate(reservationToUpdate);
            if (isOverlap) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            Reservation savedReservation = reservationService.updateReservation(id, reservationToUpdate);
            ReservationDTO savedReservationDTO = reservationService.convertToDTO(savedReservation);
            return ResponseEntity.ok(savedReservationDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        Reservation existingReservation = reservationService.getReservationById(id);

        if (existingReservation != null) {
            reservationService.deleteReservation(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
