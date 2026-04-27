import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Categoria } from '../Model/categoriaModel.model';
import { Servicios } from '../Services/servicios';
import { Router, RouterModule } from '@angular/router';
import { AuthServiceTs } from '../Auth/ServiceAuth/auth.service';

@Component({
  selector: 'app-categorias',
  imports: [CommonModule, RouterModule],
  templateUrl: './categorias.html',
  styleUrl: './categorias.css',
})
export class Categorias {

  categorias: Categoria[] = [];

  constructor(private categoriaService: Servicios, public auth: AuthServiceTs, private router: Router) {}

  ngOnInit(): void {
    this.categoriaService.getCategorias().subscribe({
      next: (data: Categoria[]) => this.categorias = data,
      error: (err: any) => console.error('Error cargando categorías', err)
    });
  }

  estaLogueado(): boolean {
    return !!this.auth.getToken();
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/']);
  }
}
