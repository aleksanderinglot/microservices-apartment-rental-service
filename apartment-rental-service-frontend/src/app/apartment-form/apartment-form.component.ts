import { Component, OnInit } from '@angular/core';
import { Apartment } from '../apartment.model';
import { ApartmentService } from '../apartment.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-apartment-form',
  templateUrl: './apartment-form.component.html',
  styleUrls: ['./apartment-form.component.css']
})
export class ApartmentFormComponent implements OnInit {
  newApartment: Apartment = {
    name: '',
    address: '',
    numberOfRooms: 0,
    price: 0
  };

  constructor(private apartmentService: ApartmentService, private router: Router) { }

  ngOnInit(): void {
  }

  addApartment(): void {
    if (!this.newApartment.name || !this.newApartment.address || !this.newApartment.numberOfRooms || !this.newApartment.price) {
      alert('Please fill in all the fields.');
      return;
    }
    
    this.apartmentService.createApartment(this.newApartment)
      .subscribe(apartment => {
        console.log('Apartment added:', apartment);
        this.router.navigate(['/apartments']);
      });
  }
}
