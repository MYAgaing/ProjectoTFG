import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Categoria } from '../Model/categoriaModel.model';
import { Producto } from '../Model/productoModel.model';

@Injectable({
  providedIn: 'root',
})
export class AdminProductoService {
  
  private baseUrl = `${environment.apiUrl}/admin/productos`;

  constructor(private http: HttpClient) { }

  getCategorias(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(`${this.baseUrl}/categorias`);
  }

  crearProducto(formData: FormData): Observable<Producto> {
    return this.http.post<Producto>(this.baseUrl, formData);
  }

  actualizarProducto(id: number, formData: FormData): Observable<Producto> {
    return this.http.put<Producto>(`${this.baseUrl}/${id}`, formData);
  }

  eliminarProducto(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }
}
