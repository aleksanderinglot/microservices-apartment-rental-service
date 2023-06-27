import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { LoginRequest } from '../login.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  showInvalidPassword: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    const request: LoginRequest = {
      email: this.email,
      password: this.password
    };

    this.authService.login(request)
    .subscribe(
      () => {
        this.router.navigate(['/apartments']);
      },
      (error) => {
        if (error.status === 401) {
          this.showInvalidPassword = true;
        } else {
          alert('Incorrect password.');
        }
      }
    );
  }
}

