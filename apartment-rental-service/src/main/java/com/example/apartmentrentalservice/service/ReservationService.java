package com.example.apartmentrentalservice.service;

import com.example.apartmentrentalservice.dto.ReservationDTO;
import com.example.apartmentrentalservice.exception.ReservationNotFoundException;
import com.example.apartmentrentalservice.model.Reservation;
import com.example.apartmentrentalservice.repository.ApartmentRepository;
import com.example.apartmentrentalservice.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository, ApartmentRepository apartmentRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation id not found - " + id));
    }

    public List<Reservation> getUserReservations(String guestEmail) {
        return reservationRepository.findByGuestEmail(guestEmail);
    }

    public Reservation createReservation(Reservation reservation) {
        LocalDate startDate = reservation.getStartDate();
        LocalDate endDate = reservation.getEndDate();

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(Long id, Reservation updatedReservation) {
        Reservation reservation = getReservationById(id);
        LocalDate startDate = updatedReservation.getStartDate();
        LocalDate endDate = updatedReservation.getEndDate();

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        reservation.setApartmentId(updatedReservation.getApartmentId());
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setGuestName(updatedReservation.getGuestName());

        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public ReservationDTO convertToDTO(Reservation reservation) {
        return ReservationDTO.builder()
                .id(reservation.getId())
                .apartmentId(reservation.getApartmentId())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .guestName(reservation.getGuestName())
                .guestEmail(reservation.getGuestEmail())
                .build();
    }

    public Reservation convertToEntity(ReservationDTO reservationDTO) {
        return Reservation.builder()
                .apartmentId(reservationDTO.getApartmentId())
                .startDate(reservationDTO.getStartDate())
                .endDate(reservationDTO.getEndDate())
                .guestName(reservationDTO.getGuestName())
                .guestEmail(reservationDTO.getGuestEmail())
                .build();
    }

    public List<ReservationDTO> convertToDTOList(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public boolean checkReservationOverlap(Reservation reservation) {
        List<Reservation> existingReservations = reservationRepository.findByApartmentId(reservation.getApartmentId());

        for (Reservation existingReservation : existingReservations) {
            if (isDateRangeOverlap(reservation.getStartDate(), reservation.getEndDate(),
                    existingReservation.getStartDate(), existingReservation.getEndDate())) {
                return true;
            }
        }

        return false;
    }


    public boolean checkReservationOverlapForUpdate(Reservation reservationToUpdate) {
        List<Reservation> existingReservations = reservationRepository.findByApartmentIdAndIdNot(reservationToUpdate.getApartmentId(), reservationToUpdate.getId());

        for (Reservation reservation : existingReservations) {
            if (isDateRangeOverlap(reservationToUpdate.getStartDate(), reservationToUpdate.getEndDate(),
                    reservation.getStartDate(), reservation.getEndDate())) {
                return true;
            }
        }

        return false;
    }

    private boolean isDateRangeOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return (start1.isBefore(end2) || start1.isEqual(end2)) && (start2.isBefore(end1) || start2.isEqual(end1));
    }
}
