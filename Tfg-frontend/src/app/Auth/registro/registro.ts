import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthServiceTs } from '../ServiceAuth/auth.service';

@Component({
  selector: 'app-registro',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './registro.html',
  styleUrl: './registro.css',
})
export class Registro {

  nombre = '';
  email = '';
  password = '';

  cargando = false;
  exito = false;
  error = '';

  constructor(private auth: AuthServiceTs) {}

  registrar() {
    this.error = '';
    this.cargando = true;

    this.auth.register(this.nombre, this.email, this.password).subscribe({
      next: () => {
        this.cargando = false;
        this.exito = true;
      },
      error: (err) => {
        this.cargando = false;
        this.error = err.error ?? 'Error al registrarse. Inténtalo de nuevo.';
      }
    });
  }
}
