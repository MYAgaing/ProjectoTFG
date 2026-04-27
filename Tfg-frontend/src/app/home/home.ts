import { CommonModule } from '@angular/common';
import { Component, OnInit, HostListener } from '@angular/core';
import { Router, RouterLink } from "@angular/router";
import { FormsModule } from '@angular/forms';
import { Producto } from '../Model/productoModel.model';
import { Servicios } from '../Services/servicios';
import { ProductoService } from '../Services/producto-service';
import { AuthServiceTs } from '../Auth/ServiceAuth/auth.service';

@Component({
  selector: 'app-home',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
})
export class Home implements OnInit {

  listaTecnologia: Producto[] = []
  listaJuegos: Producto[] = []
  listaLibros: Producto[] = []
  listaElectronica: Producto[] = []
  listaModa: Producto[] = []

  // Búsqueda
  todosLosProductos: Producto[] = []
  terminoBusqueda: string = ''
  resultadosBusqueda: Producto[] = []
  mostrarDropdown: boolean = false

  constructor(private s: Servicios, private productoService: ProductoService, private router: Router, public auth: AuthServiceTs) { }

  ngOnInit(): void {
    this.s.getProductosPorCategoria(1).subscribe((data) => { this.listaTecnologia = data; });
    this.s.getProductosPorCategoria(2).subscribe((data) => { this.listaJuegos = data; });
    this.s.getProductosPorCategoria(3).subscribe((data) => { this.listaLibros = data; });
    this.s.getProductosPorCategoria(4).subscribe((data) => { this.listaElectronica = data; });
    this.s.getProductosPorCategoria(5).subscribe((data) => { this.listaModa = data; });

    // Cargar todos los productos para la búsqueda
    this.productoService.getProductos().subscribe((data) => {
      this.todosLosProductos = data;
    });
  }

  buscar(): void {
    const termino = this.terminoBusqueda.trim().toLowerCase();
    if (termino.length === 0) {
      this.resultadosBusqueda = [];
      this.mostrarDropdown = false;
      return;
    }
    this.resultadosBusqueda = this.todosLosProductos.filter(p =>
      p.nombre.toLowerCase().includes(termino) ||
      p.marca.toLowerCase().includes(termino) ||
      p.categoria?.nombre?.toLowerCase().includes(termino)
    ).slice(0, 8);
    this.mostrarDropdown = true;
  }

  irAProducto(producto: Producto): void {
    this.terminoBusqueda = '';
    this.mostrarDropdown = false;
    this.router.navigate(['/producto', producto.idProducto]);
  }

  cerrarDropdown(): void {
    setTimeout(() => { this.mostrarDropdown = false; }, 150);
  }

  estaLogueado(): boolean {
    return !!this.auth.getToken();
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/']);
  }

  getEstrellas(puntuacion: number): number[] {
    return Array(5).fill(0).map((_, i) => i < puntuacion ? 1 : 0);
  }

}