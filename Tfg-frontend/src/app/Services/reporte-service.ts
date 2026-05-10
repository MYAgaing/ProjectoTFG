import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReporteService {

  private baseUrl = `${environment.apiUrl}/api/reportes`;
  private adminUrl = `${environment.apiUrl}/admin/reportes`;

  constructor(private http: HttpClient) {}

  private headers() {
    const token = localStorage.getItem('token');
    return { Authorization: `Bearer ${token}` };
  }

  /** Reportar una reseña */
  reportar(idResena: number, motivo: string, descripcion: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/${idResena}`,
      { motivo, descripcion },
      { headers: this.headers() }
    );
  }

  /** Comprobar si el usuario ya reportó esta reseña */
  yaReporto(idResena: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/check/${idResena}`, {
      headers: this.headers()
    });
  }

  /** ADMIN: obtener reportes pendientes */
  getPendientes(): Observable<any[]> {
    return this.http.get<any[]>(`${this.adminUrl}/pendientes`, {
      headers: this.headers()
    });
  }

  /** ADMIN: obtener todos los reportes */
  getTodos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.adminUrl}/todos`, {
      headers: this.headers()
    });
  }

  /** ADMIN: descartar reporte */
  descartar(idReporte: number): Observable<any> {
    return this.http.put(`${this.adminUrl}/${idReporte}/descartar`, {}, {
      headers: this.headers()
    });
  }

  /** ADMIN: confirmar reporte y eliminar reseña */
  eliminarResena(idReporte: number): Observable<string> {
    return this.http.put(`${this.adminUrl}/${idReporte}/eliminar-resena`, {}, {
      headers: this.headers(),
      responseType: 'text'
    });
  }
}
