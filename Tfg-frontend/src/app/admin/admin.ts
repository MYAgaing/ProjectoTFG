import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ReporteService } from '../Services/reporte-service';
import { AdminEstadisticasService } from '../Services/admin-estadisticas-service';
import { AdminProductoService } from '../Services/admin-producto-service';
import { AuthServiceTs } from '../Auth/ServiceAuth/auth.service';
import { Router } from '@angular/router';
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs';

@Component({
  selector: 'app-admin',
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class Admin implements OnInit {

  // ── Tabs ─────────────────────────────────────────────────────────────────────
  tabActivo: 'reportes' | 'estadisticas' | 'productos' = 'reportes';

  // ── Reportes ─────────────────────────────────────────────────────────────────
  reportes: any[] = [];
  cargandoReportes = true;
  errorReportes = false;
  filtro: 'PENDIENTE' | 'TODOS' = 'PENDIENTE';
  mostrarConfirm = false;
  accionPendiente: 'descartar' | 'eliminar' | null = null;
  reporteSeleccionado: any = null;
  procesando = false;
  mensajeAccion = '';

  // ── Estadísticas ─────────────────────────────────────────────────────────────
  busqueda = '';
  resultadosBusqueda: any[] = [];
  buscando = false;
  productoSeleccionado: any = null;
  stats: any = null;
  cargandoStats = false;
  errorStats = false;
  filtroEstrellas = 0; // 0 = todas
  private busqueda$ = new Subject<string>();

  // ── Gestión de Productos ────────────────────────────────────────────────────
  categorias: any[] = [];
  cargandoCategorias = false;
  mostrarFormulario = false;
  formularioProducto = {
    nombre: '',
    descripcion: '',
    marca: '',
    fechaLanzamiento: '',
    idCategoria: 0,
    imagen: null as File | null
  };
  imagenPreview: string | null = null;
  enviandoProducto = false;
  mensajeProducto = '';
  errorProducto = false;

  constructor(
    private sReporte: ReporteService,
    private sEstadisticas: AdminEstadisticasService,
    private sProducto: AdminProductoService,
    private auth: AuthServiceTs,
    private router: Router
  ) {}

  ngOnInit() {
    this.cargarReportes();

    // Búsqueda con debounce para no llamar en cada tecla
    this.busqueda$.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(term => {
      if (term.trim().length < 2) {
        this.resultadosBusqueda = [];
        return;
      }
      this.buscando = true;
      this.sEstadisticas.buscarProductos(term).subscribe({
        next: (data) => { this.resultadosBusqueda = data; this.buscando = false; },
        error: () => { this.buscando = false; }
      });
    });
  }

  // ── Reportes ─────────────────────────────────────────────────────────────────

  cargarReportes() {
    this.cargandoReportes = true;
    this.errorReportes = false;
    const obs = this.filtro === 'PENDIENTE'
      ? this.sReporte.getPendientes()
      : this.sReporte.getTodos();
    obs.subscribe({
      next: (data) => { this.reportes = data; this.cargandoReportes = false; },
      error: () => { this.errorReportes = true; this.cargandoReportes = false; }
    });
  }

  cambiarFiltro(f: 'PENDIENTE' | 'TODOS') {
    this.filtro = f;
    this.cargarReportes();
  }

  confirmarAccion(reporte: any, accion: 'descartar' | 'eliminar') {
    this.reporteSeleccionado = reporte;
    this.accionPendiente = accion;
    this.mensajeAccion = '';
    this.mostrarConfirm = true;
  }

  cancelarAccion() {
    this.mostrarConfirm = false;
    this.reporteSeleccionado = null;
    this.accionPendiente = null;
  }

  ejecutarAccion() {
    if (!this.reporteSeleccionado || !this.accionPendiente) return;
    this.procesando = true;
    const obs = this.accionPendiente === 'descartar'
      ? this.sReporte.descartar(this.reporteSeleccionado.idReporte)
      : this.sReporte.eliminarResena(this.reporteSeleccionado.idReporte);
    obs.subscribe({
      next: () => { this.procesando = false; this.cancelarAccion(); this.cargarReportes(); },
      error: () => { this.mensajeAccion = 'Error al procesar la acción.'; this.procesando = false; }
    });
  }

  motivoLabel(motivo: string): string {
    const map: any = { INAPROPIADA: 'Inapropiada', FALSA: 'Falsa / Spam', SPAM: 'Publicidad', OTRO: 'Otro' };
    return map[motivo] ?? motivo;
  }

  estadoBadge(estado: string): string {
    const map: any = { PENDIENTE: 'bg-yellow-100 text-yellow-700', REVISADA: 'bg-green-100 text-green-700', DESCARTADA: 'bg-gray-100 text-gray-500' };
    return map[estado] ?? 'bg-gray-100 text-gray-500';
  }

  // ── Estadísticas ─────────────────────────────────────────────────────────────

  onBusqueda(term: string) {
    this.busqueda$.next(term);
  }

  seleccionarProducto(producto: any) {
    this.productoSeleccionado = producto;
    this.resultadosBusqueda = [];
    this.busqueda = producto.nombre;
    this.cargarStats(producto.idProducto);
  }

  cargarStats(id: number) {
    this.cargandoStats = true;
    this.errorStats = false;
    this.stats = null;
    this.filtroEstrellas = 0;
    this.sEstadisticas.getEstadisticas(id).subscribe({
      next: (data) => { this.stats = data; this.cargandoStats = false; },
      error: () => { this.errorStats = true; this.cargandoStats = false; }
    });
  }

  /** Reseñas filtradas por estrellas */
  resenasFiltradas(): any[] {
    if (!this.stats?.ultimasResenas) return [];
    if (this.filtroEstrellas === 0) return this.stats.ultimasResenas;
    return this.stats.ultimasResenas.filter((r: any) => r.puntuacion === this.filtroEstrellas);
  }

  /** Convierte el mapa de distribución en array ordenado para la vista */
  distribucionArray(): { estrellas: number; count: number; pct: number }[] {
    if (!this.stats?.distribucion) return [];
    return [5, 4, 3, 2, 1].map(n => ({
      estrellas: n,
      count: this.stats.distribucion[n] ?? 0,
      pct: this.stats.porcentajes?.[n] ?? 0
    }));
  }

  /** Altura de barra para el gráfico de evolución */
  alturaBarraEvolucion(valor: number): number {
    if (!this.stats?.evolucionMensual) return 0;
    const max = Math.max(...this.stats.evolucionMensual.map((m: any) => m.totalResenas), 1);
    return Math.round((valor / max) * 100);
  }

  estrellas(n: number): string[] {
    return Array(5).fill(0).map((_, i) => i < Math.round(n) ? '★' : '☆');
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/']);
  }

  // ── Gestión de Productos ────────────────────────────────────────────────────

  cargarCategorias() {
    this.cargandoCategorias = true;
    this.sProducto.getCategorias().subscribe({
      next: (data) => { 
        this.categorias = data; 
        this.cargandoCategorias = false; 
      },
      error: () => { this.cargandoCategorias = false; }
    });
  }

  abrirFormulario() {
    if (this.categorias.length === 0) {
      this.cargarCategorias();
    }
    this.mostrarFormulario = true;
    this.resetFormulario();
  }

  cerrarFormulario() {
    this.mostrarFormulario = false;
    this.resetFormulario();
  }

  resetFormulario() {
    this.formularioProducto = {
      nombre: '',
      descripcion: '',
      marca: '',
      fechaLanzamiento: '',
      idCategoria: 0,
      imagen: null
    };
    this.imagenPreview = null;
    this.mensajeProducto = '';
    this.errorProducto = false;
  }

  onImagenSeleccionada(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.formularioProducto.imagen = file;
      
      // Crear preview
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imagenPreview = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  enviarProducto() {
    // Validaciones
    if (!this.formularioProducto.nombre.trim()) {
      this.mensajeProducto = 'El nombre es obligatorio';
      this.errorProducto = true;
      return;
    }
    if (!this.formularioProducto.marca.trim()) {
      this.mensajeProducto = 'La marca es obligatoria';
      this.errorProducto = true;
      return;
    }
    if (!this.formularioProducto.fechaLanzamiento) {
      this.mensajeProducto = 'La fecha de lanzamiento es obligatoria';
      this.errorProducto = true;
      return;
    }
    if (!this.formularioProducto.idCategoria || this.formularioProducto.idCategoria === 0) {
      this.mensajeProducto = 'Debes seleccionar una categoría';
      this.errorProducto = true;
      return;
    }

    // Crear FormData
    const formData = new FormData();
    formData.append('nombre', this.formularioProducto.nombre);
    formData.append('descripcion', this.formularioProducto.descripcion);
    formData.append('marca', this.formularioProducto.marca);
    formData.append('fechaLanzamiento', this.formularioProducto.fechaLanzamiento);
    formData.append('idCategoria', this.formularioProducto.idCategoria.toString());
    
    if (this.formularioProducto.imagen) {
      formData.append('imagen', this.formularioProducto.imagen);
    }

    this.enviandoProducto = true;
    this.mensajeProducto = '';
    this.errorProducto = false;

    this.sProducto.crearProducto(formData).subscribe({
      next: (producto) => {
        this.enviandoProducto = false;
        this.mensajeProducto = '¡Producto creado exitosamente!';
        this.errorProducto = false;
        setTimeout(() => {
          this.cerrarFormulario();
        }, 2000);
      },
      error: (err) => {
        this.enviandoProducto = false;
        this.mensajeProducto = 'Error al crear el producto: ' + (err.error || 'Error desconocido');
        this.errorProducto = true;
      }
    });
  }
}
