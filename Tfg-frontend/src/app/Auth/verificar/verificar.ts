import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AuthServiceTs } from '../ServiceAuth/auth.service';

@Component({
  selector: 'app-verificar',
  imports: [CommonModule, RouterLink],
  templateUrl: './verificar.html',
  styleUrl: './verificar.css',
})
export class Verificar implements OnInit {

  estado: 'cargando' | 'exito' | 'error' = 'cargando';
  mensaje = '';

  constructor(
    private route: ActivatedRoute,
    private auth: AuthServiceTs
  ) {}

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');

    if (!token) {
      this.estado = 'error';
      this.mensaje = 'Enlace de verificación inválido.';
      return;
    }

    this.auth.verificarEmail(token).subscribe({
      next: () => {
        this.estado = 'exito';
        this.mensaje = '¡Tu cuenta ha sido verificada correctamente!';
      },
      error: (err) => {
        this.estado = 'error';
        this.mensaje = err.error ?? 'El enlace es inválido o ha expirado.';
      }
    });
  }
}
