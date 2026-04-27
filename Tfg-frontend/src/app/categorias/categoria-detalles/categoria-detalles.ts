import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Producto } from '../../Model/productoModel.model';
import { Servicios } from '../../Services/servicios';
import { AuthServiceTs } from '../../Auth/ServiceAuth/auth.service';

@Component({
  selector: 'app-categoria-detalles',
  imports: [CommonModule, RouterLink],
  templateUrl: './categoria-detalles.html',
  styleUrl: './categoria-detalles.css',
})
export class CategoriaDetalles implements OnInit {

  productos: Producto[] = [];
  categoriaNombre: string = '';

  constructor(
    private route: ActivatedRoute,
    private s: Servicios,
    public auth: AuthServiceTs,
    private router: Router
  ) {}

  ngOnInit(): void {
    const idCategoria = Number(this.route.snapshot.paramMap.get('id'));
    const nombre = this.route.snapshot.paramMap.get('nombre');
    this.categoriaNombre = nombre || '';

    if (idCategoria) {
      this.s.getProductosPorCategoria(idCategoria).subscribe({
        next: (data) => this.productos = data,
        error: (err) => console.error('Error cargando productos', err)
      });
    }
  }

  getEstrellas(puntuacion: number): number[] {
    return Array(5).fill(0).map((_, i) => i < Math.round(puntuacion) ? 1 : 0);
  }

  estaLogueado(): boolean {
    return !!this.auth.getToken();
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/']);
  }
}
