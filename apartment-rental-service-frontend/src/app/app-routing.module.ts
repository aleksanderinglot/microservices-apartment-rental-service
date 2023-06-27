import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ApartmentListComponent } from './apartment-list/apartment-list.component';
import { ApartmentDetailsComponent } from './apartment-details/apartment-details.component';
import { ApartmentFormComponent } from './apartment-form/apartment-form.component';
import { ApartmentRentComponent } from './apartment-rent/apartment-rent.component';
import { RegistrationComponent } from './registration/registration.component';
import { LoginComponent } from './login/login.component';
import { UserReservationsComponent } from './user-reservations/user-reservations.component';
import { ApartmentRentEditComponent } from './apartment-rent-edit/apartment-rent-edit.component';

const routes: Routes = [
  { path: 'apartments', component: ApartmentListComponent },
  { path: 'apartments/add', component: ApartmentFormComponent },
  { path: 'apartments/edit/:id', component: ApartmentFormComponent },
  { path: 'apartments/:id', component: ApartmentDetailsComponent },
  { path: 'apartments/:id/rent', component: ApartmentRentComponent },
  { path: 'registration', component: RegistrationComponent },
  { path: 'login', component: LoginComponent },
  { path: 'user-reservations', component: UserReservationsComponent },
  { path: 'reservation-edit/:id', component: ApartmentRentEditComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
