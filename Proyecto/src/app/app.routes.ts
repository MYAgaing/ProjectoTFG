import { Routes } from '@angular/router';
import { Login } from './Auth/login/login';
import { Registro } from './Auth/registro/registro';
import { Home } from './home/home';
import { UserProfile } from './user-profile/user-profile';

export const routes: Routes = [
    { path: '', component: Home },
    { path: 'login', component: Login },
    { path: 'register', component: Registro },
    { path: 'profile', component: UserProfile },
    { path: '**', redirectTo: '' }
];
