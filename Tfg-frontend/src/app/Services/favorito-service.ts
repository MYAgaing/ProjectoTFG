import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthServiceTs } from '../Auth/ServiceAuth/auth.service';

@Injectable({
  providedIn: 'root',
})
export class FavoritoService {

  private baseUrl = 'http://localhost:8080/api/favoritos';

  constructor(private http: HttpClient, private auth: AuthServiceTs) {}

  private headers(): HttpHeaders {
    return new HttpHeaders({ Authorization: `Bearer ${this.auth.getToken()}` });
  }

  getFavoritos(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl, { headers: this.headers() });
  }

  agregarFavorito(idProducto: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/${idProducto}`, {}, { headers: this.headers() });
  }

  eliminarFavorito(idProducto: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${idProducto}`, { headers: this.headers(), responseType: 'text' as 'json' });
  }

  esFavorito(idProducto: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/check/${idProducto}`, { headers: this.headers() });
  }
}
