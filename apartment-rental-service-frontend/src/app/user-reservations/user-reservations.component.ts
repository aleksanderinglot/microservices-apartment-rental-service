import { Component, OnInit } from '@angular/core';
import { ApartmentService } from '../apartment.service';
import { AuthService } from '../auth.service';
import { Reservation } from '../reservation.model';
import { Apartment } from '../apartment.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-reservations',
  templateUrl: './user-reservations.component.html',
  styleUrls: ['./user-reservations.component.css']
})
export class UserReservationsComponent implements OnInit {
  reservations: Reservation[] = [];
  reservation: Reservation | undefined;
  apartments: Apartment[] = [];

  constructor(private apartmentService: ApartmentService, private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.getReservations();
    this.getApartments();
  }

  getReservations() {
    const username = this.authService.getUsername();
    this.apartmentService.getUserReservations(username).subscribe(
      (reservations: Reservation[]) => {
        this.reservations = reservations;
      },
      (error) => {
        console.log('Error:', error);
      }
    );
  }

  getApartments(): void {
    this.apartmentService.getApartments().subscribe(apartments => {
      this.apartments = apartments;
    });
  }

  getApartmentName(apartmentId: number): string {
    const apartment = this.apartments.find(a => a.id === apartmentId);
    return apartment ? apartment.name : '';
  }

  editReservation(reservationId: number): void {
    this.apartmentService.getReservationById(reservationId).subscribe(
      (reservation: Reservation) => {
        this.reservation = reservation;
        this.router.navigate(['/reservation-edit', reservationId]);
      },
      (error) => {
        console.log('Error:', error);
      }
    );
  }  

  deleteReservation(reservationId: number): void {
    this.apartmentService.deleteReservation(reservationId).subscribe(
      () => {
        this.getReservations();
      },
      (error) => {
        console.log('Error:', error);
      }
    );
  }
  
}


