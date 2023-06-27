import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { RegistrationRequest } from '../registration.model';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {
  username: string = '';
  email: string = '';
  password: string = '';
  role: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  register(): void {
    if (!this.username || !this.email || !this.password || !this.role) {
      alert('Please fill in all the fields.');
      return;
    }

    const request: RegistrationRequest = {
      username: this.username,
      email: this.email,
      password: this.password,
      role: this.role
    };

    this.authService.register(request)
      .subscribe(() => {
        this.router.navigate(['/apartments']);
      });
  }
}
