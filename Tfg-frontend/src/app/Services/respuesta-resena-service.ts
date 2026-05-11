import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Respuesta {
  id: number;
  texto: string;
  fecha: string;
  usuario: { idUsuario: number; nombre: string; estado: boolean };
}

@Injectable({ providedIn: 'root' })
export class RespuestaResenaService {

  private base = `${environment.apiUrl}/api/resenas`;

  constructor(private http: HttpClient) {}

  private authHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({ Authorization: `Bearer ${token ?? ''}` });
  }

  getRespuestas(idResena: number): Observable<Respuesta[]> {
    return this.http.get<Respuesta[]>(`${this.base}/${idResena}/respuestas`);
  }

  crearRespuesta(idResena: number, texto: string): Observable<Respuesta> {
    return this.http.post<Respuesta>(
      `${this.base}/${idResena}/respuestas`,
      { texto },
      { headers: this.authHeaders() }
    );
  }

  borrarRespuesta(idResena: number, idRespuesta: number): Observable<string> {
    return this.http.delete(
      `${this.base}/${idResena}/respuestas/${idRespuesta}`,
      { headers: this.authHeaders(), responseType: 'text' }
    );
  }
}
