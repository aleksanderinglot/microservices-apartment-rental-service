import { Component, OnInit } from '@angular/core';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Example';
  userLoggedIn = false;

  ngOnInit() {
    localStorage.clear();
    this.authService.isLoggedIn().subscribe((loggedIn: boolean) => {
      this.userLoggedIn = loggedIn;
    });
  }

  constructor(private authService: AuthService) {}
}
