import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  isLoggedIn: boolean = false;
  isShowUserReservations: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.authService.isLoggedIn().subscribe((isLoggedIn: boolean) => {
      this.isLoggedIn = isLoggedIn;
    });
  }

  logout(): void {
    this.authService.logout().subscribe(() => {
      this.isLoggedIn = false;
      this.router.navigate(['/login']);
      this.isShowUserReservations = false;
    });
  }

  showUserReservations(): void {
    this.isShowUserReservations = !this.isShowUserReservations;
    if (this.isShowUserReservations) {
      this.router.navigate(['/user-reservations']);
    } else {
      this.router.navigate(['/apartments']);
    }
  }
}
