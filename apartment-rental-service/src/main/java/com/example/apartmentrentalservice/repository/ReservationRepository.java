package com.example.apartmentrentalservice.repository;

import com.example.apartmentrentalservice.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByApartmentIdAndIdNot(Long apartmentId, Long id);
    List<Reservation> findByApartmentId(Long apartmentId);
    List<Reservation> findByGuestEmail(String guestEmail);
}
