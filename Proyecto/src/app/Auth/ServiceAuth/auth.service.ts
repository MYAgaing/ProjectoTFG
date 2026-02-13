import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AuthServiceTs {

  private api = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) { }

  login(email: string, password: string) {
    return this.http.post(this.api + '/login', {
      email,
      password
    }, {
      responseType: 'text'
    });
  }

  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  getToken() {
    return localStorage.getItem('token');
  }

  logout() {
    localStorage.removeItem('token');
  }

  getUserProfile() {
    const token = this.getToken();
    if (!token) return null;

    return this.http.get<any>('http://localhost:8080/usuario/me', {
      headers: { Authorization: `Bearer ${token}` }
    });
  }

}
