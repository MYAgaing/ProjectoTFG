import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

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

  register(nombre: string, email: string, password: string) {
    return this.http.post(this.api + '/register', {
      nombre,
      email,
      password,
      rolId: 1   // rol USER por defecto
    }, {
      responseType: 'text'
    });
  }

  verificarEmail(token: string) {
    return this.http.get(this.api + '/verificar', {
      params: { token },
      responseType: 'text'
    });
  }

  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    const token = localStorage.getItem('token');
    if (!token) return null;
    // Si el token ha expirado, lo eliminamos automáticamente
    if (this.isTokenExpired(token)) {
      localStorage.removeItem('token');
      return null;
    }
    return token;
  }

  logout() {
    localStorage.removeItem('token');
  }

  /** Comprueba si el JWT ha expirado */
  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      if (!payload.exp) return false;
      // exp está en segundos, Date.now() en milisegundos
      return payload.exp * 1000 < Date.now();
    } catch {
      // Token malformado → lo tratamos como expirado
      return true;
    }
  }

  /** Extrae el rol del payload del JWT sin librería externa */
  getRol(): string | null {
    const token = this.getToken();
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.rol ?? null;
    } catch {
      return null;
    }
  }

  isAdmin(): boolean {
    return this.getRol() === 'ADMIN';
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
