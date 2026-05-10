import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AdminEstadisticasService {

  private base = `${environment.apiUrl}/admin/estadisticas`;

  constructor(private http: HttpClient) {}

  private headers() {
    return { Authorization: `Bearer ${localStorage.getItem('token')}` };
  }

  buscarProductos(nombre: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/buscar`, {
      params: { nombre },
      headers: this.headers()
    });
  }

  getEstadisticas(idProducto: number): Observable<any> {
    return this.http.get<any>(`${this.base}/producto/${idProducto}`, {
      headers: this.headers()
    });
  }
}
