import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/enviroment';

@Injectable({
  providedIn: 'root',
})
export class AuthServiceTs {

  private api = `${environment.apiUrl}/auth`;

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
    return this.http.get<any>(`${environment.apiUrl}/usuario/me`, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }

  actualizarPerfil(datos: { nombre?: string; password?: string }) {
    const token = this.getToken();
    return this.http.patch(`${environment.apiUrl}/usuario/me`, datos, {
      headers: { Authorization: `Bearer ${token}` },
      responseType: 'text'
    });
  }

}
