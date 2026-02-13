import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthServiceTs } from '../ServiceAuth/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  email = '';
  password = '';
  error = '';

  constructor(
    private auth: AuthServiceTs,
    private router: Router
  ) { }

  login() {
  this.auth.login(this.email, this.password).subscribe({
    next: (token: string) => {
      this.auth.saveToken(token);
      this.router.navigate(['/profile']);
    },
    error: () => {
      this.error = 'Credenciales incorrectas';
    }
  });
}


}
