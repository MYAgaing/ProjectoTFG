import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthServiceTs } from '../Auth/ServiceAuth/auth.service';
import { ResenaService } from '../Services/resena-service';
import { FavoritoService } from '../Services/favorito-service';

@Component({
  selector: 'app-user-profile',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.css',
})
export class UserProfile implements OnInit {

  user: any = null;
  misResenas: any[] = [];
  misFavoritos: any[] = [];

  // Formulario de cambio de cuenta
  nuevoNombre = '';
  nuevaPassword = '';
  confirmarPassword = '';
  mensajeGuardado = '';
  errorGuardado = '';
  guardando = false;

  // Tab activo
  tabActivo: 'resenas' | 'favoritos' | 'ajustes' = 'resenas';

  // Modal editar reseña
  mostrarModalEditar = false;
  resenaEditando: any = null;
  editTitulo = '';
  editComentario = '';
  editPuntuacion = 0;
  editEstrellaHover = 0;
  guardandoEdicion = false;

  // Confirmación borrar
  mostrarConfirmBorrar = false;
  resenaABorrar: any = null;

  constructor(
    private auth: AuthServiceTs,
    private resenaService: ResenaService,
    private favoritoService: FavoritoService,
    private router: Router
  ) {}

  ngOnInit() {
    this.auth.getUserProfile()?.subscribe({
      next: (data) => {
        this.user = data;
        this.nuevoNombre = data.nombre;
      }
    });
    this.resenaService.getMisResenas().subscribe({
      next: (data) => this.misResenas = data,
      error: () => this.misResenas = []
    });
    this.favoritoService.getFavoritos().subscribe({
      next: (data) => this.misFavoritos = data,
      error: () => this.misFavoritos = []
    });
  }

  getStars(rating: number): string[] {
    return Array(5).fill(0).map((_, i) => i <= Math.round(rating) - 1 ? '★' : '☆');
  }

  promedioValoracion(): string {
    if (!this.misResenas.length) return '—';
    const avg = this.misResenas.reduce((s, r) => s + r.puntuacion, 0) / this.misResenas.length;
    return avg.toFixed(1);
  }

  guardarCambios() {
    this.mensajeGuardado = '';
    this.errorGuardado = '';

    if (this.nuevaPassword && this.nuevaPassword !== this.confirmarPassword) {
      this.errorGuardado = 'Las contraseñas no coinciden.';
      return;
    }

    const datos: any = {};
    if (this.nuevoNombre && this.nuevoNombre !== this.user?.nombre) datos.nombre = this.nuevoNombre;
    if (this.nuevaPassword) datos.password = this.nuevaPassword;

    if (!Object.keys(datos).length) {
      this.errorGuardado = 'No hay cambios que guardar.';
      return;
    }

    this.guardando = true;
    this.auth.actualizarPerfil(datos).subscribe({
      next: () => {
        this.mensajeGuardado = 'Cambios guardados correctamente.';
        this.guardando = false;
        this.nuevaPassword = '';
        this.confirmarPassword = '';
        if (datos.nombre) this.user.nombre = datos.nombre;
      },
      error: () => {
        this.errorGuardado = 'Error al guardar los cambios.';
        this.guardando = false;
      }
    });
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/']);
  }

  quitarFavorito(idProducto: number) {
    this.favoritoService.eliminarFavorito(idProducto).subscribe({
      next: () => this.misFavoritos = this.misFavoritos.filter(f => f.producto?.idProducto !== idProducto)
    });
  }

  // --- Editar reseña ---
  abrirEditar(resena: any) {
    this.resenaEditando = resena;
    this.editTitulo = resena.titulo;
    this.editComentario = resena.comentario;
    this.editPuntuacion = resena.puntuacion;
    this.editEstrellaHover = 0;
    this.mostrarModalEditar = true;
  }

  cerrarEditar() {
    this.mostrarModalEditar = false;
    this.resenaEditando = null;
  }

  guardarEdicion() {
    if (!this.editTitulo || !this.editComentario || this.editPuntuacion === 0) return;
    this.guardandoEdicion = true;

    const resenaActualizada = {
      ...this.resenaEditando,
      titulo: this.editTitulo,
      comentario: this.editComentario,
      puntuacion: this.editPuntuacion
    };

    this.resenaService.actualizarResena(this.resenaEditando.idResena, resenaActualizada).subscribe({
      next: (r) => {
        const idx = this.misResenas.findIndex(x => x.idResena === this.resenaEditando.idResena);
        if (idx !== -1) this.misResenas[idx] = { ...this.misResenas[idx], ...resenaActualizada };
        this.guardandoEdicion = false;
        this.cerrarEditar();
      },
      error: () => this.guardandoEdicion = false
    });
  }

  // --- Borrar reseña ---
  confirmarBorrar(resena: any) {
    this.resenaABorrar = resena;
    this.mostrarConfirmBorrar = true;
  }

  cancelarBorrar() {
    this.mostrarConfirmBorrar = false;
    this.resenaABorrar = null;
  }

  borrarResena() {
    if (!this.resenaABorrar) return;
    this.resenaService.borrarResena(this.resenaABorrar.idResena).subscribe({
      next: () => {
        this.misResenas = this.misResenas.filter(r => r.idResena !== this.resenaABorrar.idResena);
        this.cancelarBorrar();
      }
    });
  }
}
