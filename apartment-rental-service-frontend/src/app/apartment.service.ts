import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Apartment } from './apartment.model';
import { Reservation } from '../reservation.model';

@Injectable({
  providedIn: 'root'
})
export class ApartmentService {
  private apartmentsUrl = 'http://localhost:8080/api/apartments';
  private reservationsUrl = 'http://localhost:8080/api/reservations';

  constructor(private http: HttpClient) { }

  getApartments(): Observable<Apartment[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<Apartment[]>(this.apartmentsUrl, { headers });
  }

  getApartmentById(id: number): Observable<Apartment> {
    const url = `${this.apartmentsUrl}/${id}`;
    const headers = this.getAuthHeaders();
    return this.http.get<Apartment>(url, { headers });
  }

  createApartment(apartment: Apartment): Observable<Apartment> {
    const headers = this.getAuthHeaders();
    return this.http.post<Apartment>(this.apartmentsUrl, apartment, { headers });
  }

  updateApartment(id: number, apartment: Apartment): Observable<Apartment> {
    const url = `${this.apartmentsUrl}/${id}`;
    const headers = this.getAuthHeaders();
    return this.http.put<Apartment>(url, apartment, { headers });
  }

  deleteApartment(id: number): Observable<void> {
    const url = `${this.apartmentsUrl}/${id}`;
    const headers = this.getAuthHeaders();
    return this.http.delete<void>(url, { headers });
  }

  createReservation(reservation: Reservation): Observable<Reservation> {
    const headers = this.getAuthHeaders();
    return this.http.post<Reservation>(this.reservationsUrl, reservation, { headers }).pipe(
      catchError((error: any) => {
        const errorMessage = 'Error creating reservation.';
        return throwError(errorMessage);
      })
    );
  }

  getReservationById(reservationId: number): Observable<Reservation> {
    const url = `${this.reservationsUrl}/${reservationId}`;
    const headers = this.getAuthHeaders();
    return this.http.get<Reservation>(url, { headers });
  }  

  updateReservation(reservationId: number, reservation: Reservation): Observable<Reservation> {
    const url = `${this.reservationsUrl}/${reservationId}`;
    const headers = this.getAuthHeaders();
    return this.http.put<Reservation>(url, reservation, { headers }).pipe(
      catchError((error: any) => {
        const errorMessage = 'Error updating reservation.';
        return throwError(errorMessage);
      })
    );
  }

  deleteReservation(reservationId: number): Observable<void> {
    const url = `${this.reservationsUrl}/${reservationId}`;
    const headers = this.getAuthHeaders();
    return this.http.delete<void>(url, { headers });
  }

  getUserReservations(username: string): Observable<Reservation[]> {
    const url = `${this.reservationsUrl}/user/${username}`;
    const headers = this.getAuthHeaders();
    return this.http.get<Reservation[]>(url, { headers });
  }
  
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    if (token) {
      return new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });
    } else {
      return new HttpHeaders();
    }
  }
}
