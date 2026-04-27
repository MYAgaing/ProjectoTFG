import { Routes } from '@angular/router';
import { Login } from './Auth/login/login';
import { Registro } from './Auth/registro/registro';
import { Home } from './home/home';
import { UserProfile } from './user-profile/user-profile';
import { Categorias } from './categorias/categorias';
import { CategoriaDetalles } from './categorias/categoria-detalles/categoria-detalles';
import { ProductoDetalle } from './producto-detalle/producto-detalle';

export const routes: Routes = [
    { path: '', component: Home },
    { path: 'login', component: Login },
    { path: 'register', component: Registro },
    { path: 'profile', component: UserProfile },
    { path: 'categorias', component: Categorias },
    { path: 'categoria/:id/:nombre', component: CategoriaDetalles },
    { path: 'producto/:id', component: ProductoDetalle },
    { path: '**', redirectTo: '' }
];
