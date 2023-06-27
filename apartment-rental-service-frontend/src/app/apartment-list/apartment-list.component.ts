import { Component, OnInit } from '@angular/core';
import { Apartment } from '../apartment.model';
import { ApartmentService } from '../apartment.service';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-apartment-list',
  templateUrl: './apartment-list.component.html',
  styleUrls: ['./apartment-list.component.css']
})
export class ApartmentListComponent implements OnInit {
  apartments: Apartment[] = [];
  isAdmin: boolean = false;

  constructor(private apartmentService: ApartmentService, private authService: AuthService, private router: Router) { }

  ngOnInit() {
    this.getApartments();
    const userRole = this.authService.getUserRole();
    this.isAdmin = userRole === 'ADMIN' || userRole === 'admin';
  }

  addApartment(): void {
    this.router.navigate(['/apartments/add']);
  }

  getApartments(): void {
    this.apartmentService.getApartments()
      .subscribe(apartments => this.apartments = apartments);
  }

  deleteApartment(id: number): void {
    this.apartmentService.deleteApartment(id)
      .subscribe(() => {
        this.apartments = this.apartments.filter(apartment => apartment.id !== id);
      });
  }

  goToApartmentDetails(id: number | undefined): void {
    if (id !== undefined) {
      this.router.navigate(['/apartments', id]);
    } else {
      console.error('Id is undefined.');
    }
  }

  goToApartmentRent(id: number | undefined): void {
    if (id !== undefined) {
      this.router.navigate(['/apartments', id, 'rent']);
    } else {
      console.error('Id is undefined.');
    }
  }
}