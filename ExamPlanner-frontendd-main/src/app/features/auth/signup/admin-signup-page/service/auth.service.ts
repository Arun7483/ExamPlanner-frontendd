import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // Ensure this matches your Spring Boot port (default is 8080 or 8090)
  private baseUrl = 'http://localhost:8090/api/auth';

  constructor(private http: HttpClient) { }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, userData);
  }

 login(credentials: any): Observable<any> {
  // credentials will now contain { email, password, role }
  return this.http.post(`${this.baseUrl}/login`, credentials);
}
}