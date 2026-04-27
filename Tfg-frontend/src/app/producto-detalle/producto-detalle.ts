import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Producto } from '../Model/productoModel.model';
import { ProductoService } from '../Services/producto-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Resena } from '../Model/resenaModel.Model';
import { ResenaService } from '../Services/resena-service';
import { AuthServiceTs } from '../Auth/ServiceAuth/auth.service';
import { FavoritoService } from '../Services/favorito-service';

@Component({
  selector: 'app-producto-detalle',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './producto-detalle.html',
  styleUrl: './producto-detalle.css',
})
export class ProductoDetalle implements OnInit {

  product: Producto = {
    idProducto: 0, nombre: '', descripcion: '', marca: '',
    fechaLanzamiento: '',
    categoria: { idCategoria: 0, nombre: '', descripcion: '', imageUrl: '' },
    imageUrl: '', valoracion: 0
  };

  listaResenas: Resena[] = [];
  listaResenasFiltradas: Resena[] = [];

  // Filtros
  filtroOrden = '';
  filtroPuntuacion = 0;

  // Modal reseña
  mostrarModalResena = false;
  nuevaResena = { titulo: '', comentario: '', puntuacion: 0 };
  estrellaHover = 0;
  enviandoResena = false;
  mensajeResena = '';

  // Favoritos
  esFavorito = false;
  cargandoFavorito = false;

  constructor(
    private route: ActivatedRoute,
    private sProducto: ProductoService,
    private sResena: ResenaService,
    private sFavorito: FavoritoService,
    public auth: AuthServiceTs,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.sProducto.getProducto(Number(id)).subscribe(data => this.product = data);
      this.cargarResenas(Number(id));
      if (this.estaLogueado()) {
        this.sFavorito.esFavorito(Number(id)).subscribe({
          next: (val) => this.esFavorito = val,
          error: () => this.esFavorito = false
        });
      }
    }
  }

  cargarResenas(idProducto: number): void {
    this.sResena.getResenasPorProducto(idProducto, this.filtroOrden, this.filtroPuntuacion).subscribe({
      next: (data) => {
        this.listaResenas = data;
        this.listaResenasFiltradas = data;
      },
      error: () => this.listaResenas = []
    });
  }

  aplicarFiltros(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) this.cargarResenas(Number(id));
  }

  // --- Auth ---
  estaLogueado(): boolean {
    return !!this.auth.getToken();
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/']);
  }

  // --- Estrellas ---
  getStars(rating: number): string[] {
    return Array(5).fill(0).map((_, i) => i < Math.round(rating) ? '★' : '☆');
  }

  // --- Modal reseña ---
  abrirModalResena(): void {
    if (!this.estaLogueado()) { this.router.navigate(['/login']); return; }
    this.nuevaResena = { titulo: '', comentario: '', puntuacion: 0 };
    this.mensajeResena = '';
    this.mostrarModalResena = true;
  }

  cerrarModalResena(): void {
    this.mostrarModalResena = false;
  }

  setPuntuacion(n: number): void {
    this.nuevaResena.puntuacion = n;
  }

  enviarResena(): void {
    if (!this.nuevaResena.titulo || !this.nuevaResena.comentario || this.nuevaResena.puntuacion === 0) {
      this.mensajeResena = 'Por favor completa todos los campos y selecciona una puntuación.';
      return;
    }
    this.enviandoResena = true;
    const resena: Resena = {
      titulo: this.nuevaResena.titulo,
      comentario: this.nuevaResena.comentario,
      puntuacion: this.nuevaResena.puntuacion,
      fecha: new Date().toISOString().split('T')[0],
      usuario: null,
      producto: this.product
    };
    this.sResena.crearResena(resena).subscribe({
      next: (r) => {
        this.listaResenas = [r, ...this.listaResenas];
        this.enviandoResena = false;
        this.mostrarModalResena = false;
      },
      error: () => {
        this.mensajeResena = 'Error al enviar la reseña. Inténtalo de nuevo.';
        this.enviandoResena = false;
      }
    });
  }

  // --- Favoritos ---
  toggleFavorito(): void {
    if (!this.estaLogueado()) { this.router.navigate(['/login']); return; }
    this.cargandoFavorito = true;
    const accion = this.esFavorito
      ? this.sFavorito.eliminarFavorito(this.product.idProducto)
      : this.sFavorito.agregarFavorito(this.product.idProducto);

    accion.subscribe({
      next: () => {
        this.esFavorito = !this.esFavorito;
        this.cargandoFavorito = false;
      },
      error: () => this.cargandoFavorito = false
    });
  }
}
