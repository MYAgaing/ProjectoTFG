import { Component, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { AuthServiceTs } from './Auth/ServiceAuth/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, HttpClientModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected readonly title = signal('Proyecto');

  constructor(private auth: AuthServiceTs) {}

  ngOnInit(): void {
    // Al arrancar la app, getToken() ya limpia automáticamente
    // cualquier token expirado que hubiera en localStorage
    this.auth.getToken();
  }
}
