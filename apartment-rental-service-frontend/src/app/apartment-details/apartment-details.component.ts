import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Apartment } from '../apartment.model';
import { ApartmentService } from '../apartment.service';

@Component({
  selector: 'app-apartment-details',
  templateUrl: './apartment-details.component.html',
  styleUrls: ['./apartment-details.component.css']
})
export class ApartmentDetailsComponent implements OnInit {
  apartment: Apartment | undefined;

  constructor(
    private route: ActivatedRoute,
    private apartmentService: ApartmentService
  ) {}

  ngOnInit(): void {
    this.getApartmentDetails();
  }

  getApartmentDetails(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.apartmentService.getApartmentById(id)
      .subscribe(apartment => this.apartment = apartment);
  }
}

