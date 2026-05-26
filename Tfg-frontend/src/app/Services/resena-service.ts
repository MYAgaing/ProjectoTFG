import { Injectable } from '@angular/core';
import { Resena } from '../Model/resenaModel.Model';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ResenaService {

  private baseUrl = `${environment.apiUrl}/api/resenas`;

  constructor(private http: HttpClient) { }

  // GET: Todas las reseñas
  getResenas(): Observable<Resena[]> {
    return this.http.get<Resena[]>(this.baseUrl);
  }

  // GET: Una reseña por ID
  getResena(id: number): Observable<Resena> {
    return this.http.get<Resena>(`${this.baseUrl}/${id}`);
  }

  crearResena(resena: Resena): Observable<Resena> {
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.post<Resena>(this.baseUrl, resena, { headers });
  }

  actualizarResena(id: number, resena: Resena): Observable<Resena> {
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.put<Resena>(`${this.baseUrl}/${id}`, resena, { headers });
  }

  borrarResena(id: number): Observable<string> {
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.delete(`${this.baseUrl}/${id}`, { headers, responseType: 'text' });
  }

  filtrarPorFechas(inicio: string, fin: string): Observable<Resena[]> {
    const params = new HttpParams()
      .set('inicio', inicio)
      .set('fin', fin);
    return this.http.get<Resena[]>(`${this.baseUrl}/filtro-fechas`, { params });
  }

  filtrarPorPuntuacion(min: number): Observable<Resena[]> {
    return this.http.get<Resena[]>(`${this.baseUrl}/puntuacion/${min}`);
  }

  ordenarPorPuntuacionDesc(): Observable<Resena[]> {
    return this.http.get<Resena[]>(`${this.baseUrl}/orden-desc`);
  }

  ordenarPorPuntuacionAsc(): Observable<Resena[]> {
    return this.http.get<Resena[]>(`${this.baseUrl}/orden-asc`);
  }

  // Acepta orden y puntuación mínima como filtros opcionales
  getResenasPorProducto(idProducto: number, orden?: string, minPuntuacion?: number): Observable<any[]> {
    let params: any = {};
    if (orden) params.orden = orden;
    if (minPuntuacion && minPuntuacion > 0) params.minPuntuacion = minPuntuacion.toString();
    
    return this.http.get<any[]>(`${this.baseUrl}/producto/${idProducto}`, { params });
  }

  // Devuelve las 6 reseñas con mejor puntuación para mostrar en el home
  getResenasDestacadas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/destacadas`);
  }

  getMisResenas(): Observable<any[]> {
    const token = localStorage.getItem('token');
    return this.http.get<any[]>(`${this.baseUrl}/mis-resenas`, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }

}
