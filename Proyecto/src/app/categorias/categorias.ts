import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Categoria } from '../Model/categoriaModel.model';
import { Servicios } from '../Services/servicios';

@Component({
  selector: 'app-categorias',
  imports: [CommonModule],
  templateUrl: './categorias.html',
  styleUrl: './categorias.css',
})
export class Categorias {

  categorias: Categoria[] = [];

  constructor(private categoriaService: Servicios) {}

  ngOnInit(): void {
    this.categoriaService.getCategorias().subscribe({
      next: (data: Categoria[]) => this.categorias = data,
      error: (err: any) => console.error('Error cargando categorías', err)
    });
  }
}
