import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface VotoResponse {
  util: number;
  noUtil: number;
  miVoto: string; // "UTIL" | "NO_UTIL" | ""
}

@Injectable({ providedIn: 'root' })
export class VotoUtilService {

  private base = `${environment.apiUrl}/api/resenas`;

  constructor(private http: HttpClient) {}

  private authHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return token
      ? new HttpHeaders({ Authorization: `Bearer ${token}` })
      : new HttpHeaders();
  }

  getVotos(idResena: number): Observable<VotoResponse> {
    return this.http.get<VotoResponse>(
      `${this.base}/${idResena}/util`,
      { headers: this.authHeaders() }
    );
  }

  toggleVoto(idResena: number, tipo: 'UTIL' | 'NO_UTIL'): Observable<VotoResponse> {
    return this.http.post<VotoResponse>(
      `${this.base}/${idResena}/util`,
      { tipo },
      { headers: this.authHeaders() }
    );
  }
}
