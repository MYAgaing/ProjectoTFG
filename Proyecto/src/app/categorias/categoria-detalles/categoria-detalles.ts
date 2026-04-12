import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Producto } from '../../Model/productoModel.model';
import { Servicios } from '../../Services/servicios';


@Component({
  selector: 'app-categoria-detalles',
  imports: [CommonModule,RouterLink],
  templateUrl: './categoria-detalles.html',
  styleUrl: './categoria-detalles.css',
})
export class CategoriaDetalles implements OnInit{

  productos: Producto[] = [];
  categoriaNombre: string = '';
  producto: any;
  rese: any;
  relacionados: any;

  constructor(
    private route: ActivatedRoute,
    private s: Servicios,
  ) {}

  ngOnInit(): void {

    const idCategoria = Number(this.route.snapshot.paramMap.get('id'));
    const nombre = this.route.snapshot.paramMap.get('nombre');

    this.categoriaNombre = nombre || '';

    if (idCategoria) {
      this.s.getProductosPorCategoria(idCategoria)
        .subscribe({
          next: (data) => {
           this.productos = data, 
           console.log(data)
          },
          error: (err) => console.error('Error cargando productos', err)
        });
    }
  }

}
