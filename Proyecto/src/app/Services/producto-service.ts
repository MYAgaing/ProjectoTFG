import { Injectable } from '@angular/core';
import { Producto } from '../Model/productoModel.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ProductoService {
  
  private baseUrl = 'http://localhost:8080/api/productos';

  constructor(private http: HttpClient) { }

  // GET: Obtener todos los productos
  getProductos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.baseUrl);
  }

  // GET: Obtener un producto por ID
  getProducto(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.baseUrl}/${id}`);
  }

  // POST: Crear un nuevo producto
  crearProducto(producto: Producto): Observable<string> {
    // Usamos { responseType: 'text' } porque tu backend devuelve un String, no un JSON
    return this.http.post(`${this.baseUrl}`, producto, { responseType: 'text' });
  }

  // PUT: Actualizar un producto
  actualizarProducto(id: number, producto: Producto): Observable<string> {
    return this.http.put(`${this.baseUrl}/${id}`, producto, { responseType: 'text' });
  }

  // DELETE: Eliminar un producto
  borrarProducto(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' });
  }

  // GET: Filtrar por categoría
  getProductosPorCategoria(idCategoria: number): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.baseUrl}/categoria/${idCategoria}`);
  }

}
