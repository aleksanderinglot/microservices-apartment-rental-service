import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Apartment } from '../apartment.model';
import { Reservation } from '../reservation.model';
import { ApartmentService } from '../apartment.service';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-apartment-rent',
  templateUrl: './apartment-rent.component.html',
  styleUrls: ['./apartment-rent.component.css']
})
export class ApartmentRentComponent implements OnInit {
  apartmentId: number = 0;
  geustName = '';
  guestEmail = '';
  reservation: Reservation = {
    apartmentId: 0,
    guestName: '',
    guestEmail: '',
    startDate: new Date(),
    endDate: new Date()
  };
  apartmentAlreadyBooked: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private apartmentService: ApartmentService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    this.getApartmentIdFromRoute();
    const reservationId = Number(this.route.snapshot.paramMap.get('reservationId'));
    if (reservationId) {
      this.getReservation(reservationId);
    } else {
      this.initReservation();
    }
  }

  getReservation(reservationId: number): void {
    this.apartmentService.getReservationById(reservationId)
      .subscribe(
        (reservation: Reservation) => {
          this.reservation = reservation;
        },
        (error) => {
          console.log('Error:', error);
        }
      );
  }

  getApartmentIdFromRoute(): void {
    this.apartmentId = Number(this.route.snapshot.paramMap.get('id'));
  }

  initReservation(): void {
    this.reservation = {
      apartmentId: this.apartmentId,
      guestName: '',
      guestEmail: '',
      startDate: new Date(),
      endDate: new Date()
    };
  }

  rentApartment(): void {
      if (!this.reservation.guestName || !this.reservation.startDate || !this.reservation.endDate) {
        alert('Please fill in all the fields.');
        return;
      }

      this.reservation.guestEmail = this.authService.getUsername();
      let validationError = false;

      const currentDate = new Date();
      const currentDateFormatted = currentDate.toISOString().split('T')[0];
      const startDate = new Date(this.reservation.startDate);

      if (this.reservation.startDate > this.reservation.endDate) {
        validationError = true;
      }

      if (startDate.getTime() < currentDate.getTime()) {
        validationError = true;
      }

      if (!validationError) {
        this.apartmentService.createReservation(this.reservation)
          .subscribe({
            next: () => {
              this.router.navigate(['/apartments']);
            },
            error: (error) => {
              this.apartmentAlreadyBooked = true;
            }
          });
      } else {
        this.apartmentAlreadyBooked = false;
      }
    }
}
