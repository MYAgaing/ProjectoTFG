import { Injectable } from '@angular/core';
import { Resena } from '../Model/resenaModel.Model';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ResenaService {

  private baseUrl = 'http://localhost:8080/api/resenas'; // Ajusta según tu controller

  constructor(private http: HttpClient) { }

  // GET: Todas las reseñas
  getResenas(): Observable<Resena[]> {
    return this.http.get<Resena[]>(this.baseUrl);
  }

  // GET: Una reseña por ID
  getResena(id: number): Observable<Resena> {
    return this.http.get<Resena>(`${this.baseUrl}/${id}`);
  }

  // POST: Crear reseña con token JWT
  crearResena(resena: Resena): Observable<Resena> {
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.post<Resena>(this.baseUrl, resena, { headers });
  }

  // PUT: Actualizar reseña con token JWT
  actualizarResena(id: number, resena: Resena): Observable<Resena> {
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.put<Resena>(`${this.baseUrl}/${id}`, resena, { headers });
  }

  // DELETE: Borrar reseña con token JWT
  borrarResena(id: number): Observable<string> {
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.delete(`${this.baseUrl}/${id}`, { headers, responseType: 'text' });
  }

  // --- MÉTODOS DE FILTRADO Y ORDENACIÓN ---

  // GET: Filtrar por rango de fechas
  filtrarPorFechas(inicio: string, fin: string): Observable<Resena[]> {
    const params = new HttpParams()
      .set('inicio', inicio)
      .set('fin', fin);
    return this.http.get<Resena[]>(`${this.baseUrl}/filtro-fechas`, { params });
  }

  // GET: Filtrar por puntuación mínima
  filtrarPorPuntuacion(min: number): Observable<Resena[]> {
    return this.http.get<Resena[]>(`${this.baseUrl}/puntuacion/${min}`);
  }

  // GET: Ordenar Descendente
  ordenarPorPuntuacionDesc(): Observable<Resena[]> {
    return this.http.get<Resena[]>(`${this.baseUrl}/orden-desc`);
  }

  // GET: Ordenar Ascendente
  ordenarPorPuntuacionAsc(): Observable<Resena[]> {
    return this.http.get<Resena[]>(`${this.baseUrl}/orden-asc`);
  }

  // GET: Obtener solo las reseñas de un producto específico con filtros opcionales
  getResenasPorProducto(idProducto: number, orden?: string, minPuntuacion?: number): Observable<any[]> {
    let params: any = {};
    if (orden) params.orden = orden;
    if (minPuntuacion && minPuntuacion > 0) params.minPuntuacion = minPuntuacion.toString();
    
    return this.http.get<any[]>(`${this.baseUrl}/producto/${idProducto}`, { params });
  }

  // GET: Mis reseñas (requiere token)
  getMisResenas(): Observable<any[]> {
    const token = localStorage.getItem('token');
    return this.http.get<any[]>(`${this.baseUrl}/mis-resenas`, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }

}
