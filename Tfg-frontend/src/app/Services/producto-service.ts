import { Injectable } from '@angular/core';
import { Producto } from '../Model/productoModel.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ProductoService {
  
  private baseUrl = `${environment.apiUrl}/api/productos`;

  constructor(private http: HttpClient) { }

  getProductos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.baseUrl);
  }

  getProducto(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.baseUrl}/${id}`);
  }

  crearProducto(producto: Producto): Observable<string> {
    // responseType 'text' porque el backend devuelve un String, no un JSON
    return this.http.post(`${this.baseUrl}`, producto, { responseType: 'text' });
  }

  actualizarProducto(id: number, producto: Producto): Observable<string> {
    return this.http.put(`${this.baseUrl}/${id}`, producto, { responseType: 'text' });
  }

  borrarProducto(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }

  getProductosPorCategoria(idCategoria: number): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.baseUrl}/categoria/${idCategoria}`);
  }

}
