import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Producto } from '../Model/productoModel.model';
import { ProductoService } from '../Services/producto-service';
import { CommonModule } from '@angular/common';
import { Resena } from '../Model/resenaModel.Model';
import { ResenaService } from '../Services/resena-service';
import { AuthServiceTs } from '../Auth/ServiceAuth/auth.service';

@Component({
  selector: 'app-producto-detalle',
  imports: [CommonModule, RouterLink],
  templateUrl: './producto-detalle.html',
  styleUrl: './producto-detalle.css',
})
export class ProductoDetalle {

  product: Producto = {
    idProducto: 0,
    nombre: '',
    descripcion: '',
    marca: '',
    fechaLanzamiento: '',
    categoria: {
      idCategoria: 0,
      nombre: '',
      descripcion: '',
      imageUrl: ''
    },
    imageUrl: '',
    valoracion: 0
  };

  listaResenas: Resena[] = []
  constructor(private route: ActivatedRoute, private sProducto: ProductoService, private sResena: ResenaService, public auth: AuthServiceTs, private router: Router) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.sProducto.getProducto(Number(id)).subscribe((data) => {
        this.product = data;
      });
      this.sResena.getResenasPorProducto(Number(id)).subscribe((data) => {
        this.listaResenas = data;
      })
    }
  }

  getStars(rating: number): string[] {
    const stars: string[] = [];
    for (let i = 1; i <= 5; i++) {
      stars.push(i <= Math.round(rating) ? '★' : '☆');
    }
    return stars;
  }

  estaLogueado(): boolean {
    return !!this.auth.getToken();
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/']);
  }

}

