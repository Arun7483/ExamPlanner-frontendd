import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { apiConfig } from '../../../../../config/api.config';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = `${apiConfig.apiBaseUrl}/api/auth`;

  constructor(private http: HttpClient) { }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, userData);
  }

 login(credentials: any): Observable<any> {
  // credentials will now contain { email, password, role }
  return this.http.post(`${this.baseUrl}/login`, credentials);
}
}