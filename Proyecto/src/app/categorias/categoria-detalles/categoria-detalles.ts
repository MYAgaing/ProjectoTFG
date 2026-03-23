import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-categoria-detalles',
  imports: [],
  templateUrl: './categoria-detalles.html',
  styleUrl: './categoria-detalles.css',
})
export class CategoriaDetalles implements OnInit{

  productos: any[] = [];
  categoriaNombre: string = '';

  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit(): void {
    const idCategoria = this.route.snapshot.paramMap.get('id');
    const nombre = this.route.snapshot.paramMap.get('nombre');
    if (idCategoria) {
      this.categoriaNombre = nombre || '';
      this.http.get<any[]>(`http://localhost:8080/api/productos/categoria/${idCategoria}`)
        .subscribe({
          next: data => this.productos = data,
          error: err => console.error('Error cargando productos', err)
        });
    }
  }

}
