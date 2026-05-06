import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Categoria } from '../Model/categoriaModel.model';
import { Producto } from '../Model/productoModel.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class Servicios {

  private apiUrl = `${environment.apiUrl}/api/categorias`;
  private apiUrlProducto = `${environment.apiUrl}/api/productos`;

  constructor(private http: HttpClient) {}

  getCategorias(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(this.apiUrl);
  }

  getProductosPorCategoria(idCategoria: number): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.apiUrlProducto}/categoria/${idCategoria}`);
  }

}
