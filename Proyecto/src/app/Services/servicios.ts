import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Categoria } from '../Model/categoriaModel.model';

@Injectable({
  providedIn: 'root',
})
export class Servicios {

  private apiUrl = 'http://localhost:8080/api/categorias';

  constructor(private http: HttpClient) {}

  getCategorias(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(this.apiUrl);
  }

}
