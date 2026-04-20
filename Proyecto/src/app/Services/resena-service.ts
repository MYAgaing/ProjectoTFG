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

  // POST: Crear reseña (Tu backend devuelve el objeto guardado)
  crearResena(resena: Resena): Observable<Resena> {
    return this.http.post<Resena>(this.baseUrl, resena);
  }

  // PUT: Actualizar reseña
  actualizarResena(id: number, resena: Resena): Observable<Resena> {
    return this.http.put<Resena>(`${this.baseUrl}/${id}`, resena);
  }

  // DELETE: Borrar reseña (Devuelve un String)
  borrarResena(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
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

  // GET: Obtener solo las reseñas de un producto específico
getResenasPorProducto(idProducto: number): Observable<Resena[]> {
  return this.http.get<Resena[]>(`${this.baseUrl}/producto/${idProducto}`);
}

}
