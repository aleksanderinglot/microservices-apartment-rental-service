package com.example.apartmentrentalservice.controller;

import com.example.apartmentrentalservice.client.NotificationClient;
import com.example.apartmentrentalservice.dto.NotificationDTO;
import com.example.apartmentrentalservice.dto.ReservationDTO;
import com.example.apartmentrentalservice.model.Reservation;
import com.example.apartmentrentalservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;
    private final NotificationClient notificationClient;

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

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setRecipient(savedReservation.getGuestEmail());
        notificationDTO.setTitle("Potwierdzenie utworzenia rezerwacji");
        notificationDTO.setContent("Twoja rezerwacja o ID " + savedReservation.getId() + " została pomyślnie utworzona.");

        notificationClient.sendNotification(notificationDTO);
        notificationClient.processNotification(notificationDTO);
        //notificationClient.scheduleNotification(notificationDTO, LocalDateTime.now().plusHours(1).toString());

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

            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setRecipient(savedReservation.getGuestEmail());
            notificationDTO.setTitle("Aktualizacja rezerwacji");
            notificationDTO.setContent("Twoja rezerwacja o ID " + savedReservation.getId() + " została zaktualizowana.");

            notificationClient.sendNotification(notificationDTO);
            notificationClient.processNotification(notificationDTO);
            //notificationClient.scheduleNotification(notificationDTO, LocalDateTime.now().plusHours(1).toString());

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

            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setRecipient(existingReservation.getGuestEmail());;
            notificationDTO.setTitle("Usunięcie rezerwacji");
            notificationDTO.setContent("Twoja rezerwacja o ID " + id + " została usunięta.");

            notificationClient.sendNotification(notificationDTO);
            notificationClient.processNotification(notificationDTO);
            notificationClient.scheduleNotification(notificationDTO, LocalDateTime.now().plusHours(1).toString());

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
