import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

import { RegistrationRequest } from './registration.model';
import { LoginRequest } from './login.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private isLoggedInSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private userRoleSubject: BehaviorSubject<string> = new BehaviorSubject<string>('');

  constructor(private http: HttpClient) {}

  register(request: RegistrationRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, request);
  }

  login(request: LoginRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, request).pipe(
      tap((response: any) => {
        localStorage.setItem('token', response.access_token);
        localStorage.setItem('username', response.username);
        localStorage.setItem('role', response.role);
        this.isLoggedInSubject.next(true);
      })
    );
  }

  logout(): Observable<any> {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
    this.isLoggedInSubject.next(false);
    return this.http.post(`${this.apiUrl}/logout`, null).pipe(
      tap(() => {
        
      })
    );
  }
  
  isLoggedIn(): Observable<boolean> {
    return this.isLoggedInSubject.asObservable();
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

  private addAuthHeaders(headers: HttpHeaders): HttpHeaders {
    const authHeaders = this.getAuthHeaders();
    return headers.set('Authorization', authHeaders.get('Authorization') || '');
  }

  getUsername(): string {
    const username = localStorage.getItem('username');
    return username ? username : '';
  }
    
  getUserRole(): string | null {
    return localStorage.getItem('role');
  }  
}
