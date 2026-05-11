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
import { ReporteService } from '../Services/reporte-service';
import { VotoUtilService } from '../Services/voto-util-service';
import { RespuestaResenaService, Respuesta } from '../Services/respuesta-resena-service';

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

  // Reporte
  mostrarModalReporte = false;
  resenaAReportar: any = null;
  motivoReporte = 'INAPROPIADA';
  descripcionReporte = '';
  enviandoReporte = false;
  mensajeReporte = '';
  resenasReportadas = new Set<number>();

  // Votos útil — mapa idResena -> { util, noUtil, miVoto, cargando }
  votosMap = new Map<number, { util: number; noUtil: number; miVoto: string; cargando: boolean }>();

  // Respuestas — mapa idResena -> { lista, visible, texto, enviando }
  respuestasMap = new Map<number, { lista: Respuesta[]; visible: boolean; texto: string; enviando: boolean }>();

  constructor(
    private route: ActivatedRoute,
    private sProducto: ProductoService,
    private sResena: ResenaService,
    private sFavorito: FavoritoService,
    private sReporte: ReporteService,
    private sVoto: VotoUtilService,
    private sRespuesta: RespuestaResenaService,
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
        // Cargar votos para cada reseña
        data.forEach((r: any) => {
          if (r.idResena) {
            this.sVoto.getVotos(r.idResena).subscribe({
              next: (v) => this.votosMap.set(r.idResena, { ...v, cargando: false }),
              error: () => this.votosMap.set(r.idResena, { util: 0, noUtil: 0, miVoto: '', cargando: false })
            });
          }
        });
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

  // --- Reporte ---
  abrirModalReporte(resena: any): void {
    if (!this.estaLogueado()) { this.router.navigate(['/login']); return; }
    this.resenaAReportar = resena;
    this.motivoReporte = 'INAPROPIADA';
    this.descripcionReporte = '';
    this.mensajeReporte = '';
    this.mostrarModalReporte = true;
  }

  cerrarModalReporte(): void {
    this.mostrarModalReporte = false;
    this.resenaAReportar = null;
  }

  enviarReporte(): void {
    if (!this.resenaAReportar || !this.resenaAReportar.idResena) return;
    this.enviandoReporte = true;
    this.sReporte.reportar(this.resenaAReportar.idResena, this.motivoReporte, this.descripcionReporte).subscribe({
      next: () => {
        this.resenasReportadas.add(this.resenaAReportar.idResena);
        this.enviandoReporte = false;
        this.cerrarModalReporte();
      },
      error: (err) => {
        this.mensajeReporte = err.status === 409
          ? 'Ya has reportado esta reseña anteriormente.'
          : 'Error al enviar el reporte. Inténtalo de nuevo.';
        this.enviandoReporte = false;
      }
    });
  }

  yaReporto(idResena: number | undefined): boolean {
    if (idResena === undefined) return false;
    return this.resenasReportadas.has(idResena);
  }

  // --- Votos útil ---
  getVoto(idResena: number | undefined): { util: number; noUtil: number; miVoto: string; cargando: boolean } {
    if (!idResena) return { util: 0, noUtil: 0, miVoto: '', cargando: false };
    return this.votosMap.get(idResena) ?? { util: 0, noUtil: 0, miVoto: '', cargando: false };
  }

  toggleVoto(resena: any, tipo: 'UTIL' | 'NO_UTIL'): void {
    if (!this.estaLogueado()) { this.router.navigate(['/login']); return; }
    const id = resena.idResena;
    if (!id) return;
    const actual = this.getVoto(id);
    if (actual.cargando) return;
    this.votosMap.set(id, { ...actual, cargando: true });
    this.sVoto.toggleVoto(id, tipo).subscribe({
      next: (v) => this.votosMap.set(id, { ...v, cargando: false }),
      error: () => this.votosMap.set(id, { ...actual, cargando: false })
    });
  }

  // --- Respuestas ---
  getRespuestas(idResena: number | undefined) {
    if (!idResena) return { lista: [], visible: false, texto: '', enviando: false };
    return this.respuestasMap.get(idResena) ?? { lista: [], visible: false, texto: '', enviando: false };
  }

  toggleRespuestas(resena: any): void {
    const id = resena.idResena;
    if (!id) return;
    const actual = this.getRespuestas(id);
    if (!actual.visible && actual.lista.length === 0) {
      // Inicializar entrada en el mapa antes de cargar
      this.respuestasMap.set(id, { lista: [], visible: false, texto: '', enviando: false });
      this.sRespuesta.getRespuestas(id).subscribe({
        next: (lista) => this.respuestasMap.set(id, { lista, visible: true, texto: '', enviando: false }),
        error: () => this.respuestasMap.set(id, { lista: [], visible: true, texto: '', enviando: false })
      });
    } else {
      this.respuestasMap.set(id, { ...actual, visible: !actual.visible });
    }
  }

  setTextoRespuesta(idResena: number | undefined, texto: string): void {
    if (!idResena) return;
    const actual = this.getRespuestas(idResena);
    this.respuestasMap.set(idResena, { ...actual, texto });
  }

  enviarRespuesta(resena: any): void {
    if (!this.estaLogueado()) { this.router.navigate(['/login']); return; }
    const id = resena.idResena;
    if (!id) return;
    const actual = this.getRespuestas(id);
    if (!actual.texto.trim() || actual.enviando) return;
    this.respuestasMap.set(id, { ...actual, enviando: true });
    this.sRespuesta.crearRespuesta(id, actual.texto).subscribe({
      next: (r) => this.respuestasMap.set(id, {
        lista: [...actual.lista, r],
        visible: true,
        texto: '',
        enviando: false
      }),
      error: () => this.respuestasMap.set(id, { ...actual, enviando: false })
    });
  }

  borrarRespuesta(resena: any, respuesta: Respuesta): void {
    const id = resena.idResena;
    if (!id) return;
    this.sRespuesta.borrarRespuesta(id, respuesta.id).subscribe({
      next: () => {
        const actual = this.getRespuestas(id);
        this.respuestasMap.set(id, {
          ...actual,
          lista: actual.lista.filter(r => r.id !== respuesta.id)
        });
      }
    });
  }

  getMiId(): number | null {
    return this.auth.getMiId();
  }
}
