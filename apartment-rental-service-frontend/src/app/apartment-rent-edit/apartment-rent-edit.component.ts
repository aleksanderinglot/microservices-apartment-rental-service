import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Reservation } from '../reservation.model';
import { ApartmentService } from '../apartment.service';

@Component({
  selector: 'app-apartment-rent-edit',
  templateUrl: './apartment-rent-edit.component.html',
  styleUrls: ['./apartment-rent-edit.component.css']
})
export class ApartmentRentEditComponent implements OnInit {
  reservationId: number = 0;
  reservation: Reservation = {
    apartmentId: 0,
    guestName: '',
    guestEmail: '',
    startDate: new Date(),
    endDate: new Date()
  };

  constructor(
    private route: ActivatedRoute,
    private apartmentService: ApartmentService,
    private router: Router
  ) { }

  ngOnInit() {
    this.getReservationIdFromRoute();
    this.getReservation();
  }

  getReservationIdFromRoute(): void {
    this.reservationId = Number(this.route.snapshot.paramMap.get('id'));
  }

  getReservation(): void {
    this.apartmentService.getReservationById(this.reservationId)
      .subscribe((reservation: Reservation) => {
        this.reservation = reservation;
      });
  }

  updateReservation(): void {
      if (!this.reservation.guestName || !this.reservation.startDate || !this.reservation.endDate) {
        alert('Please fill in all the fields.');
        return;
      }

      let validationError = false;
      const currentDate = new Date();
      const startDate = new Date(this.reservation.startDate);

      if (this.reservation.startDate > this.reservation.endDate) {
        validationError = true;
      }

      if (startDate.getTime() < currentDate.getTime()) {
        validationError = true;
      }

      if (!validationError) {
        this.apartmentService.updateReservation(this.reservationId, this.reservation)
        .subscribe(() => {
          this.router.navigate(['/user-reservations']);
        });
      }
    }
}
