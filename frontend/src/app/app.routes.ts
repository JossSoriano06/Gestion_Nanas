import { Routes } from '@angular/router';

import { Home } from './pages/home/home';
import { Buscar } from './pages/buscar/buscar';
import { ComoFunciona } from './pages/como-funciona/como-funciona';
import { Nosotros } from './pages/nosotros/nosotros';
import { Contacto } from './pages/contacto/contacto';
import { PerfilNana } from './pages/perfil-nana/perfil-nana';
import { Login } from './pages/login/login';
import { Registro } from './pages/registro/registro';
import { RegistroNana } from './pages/registro-nana/registro-nana';

import { AdminDashboard } from './pages/admin-dashboard/admin-dashboard';
import { ClienteDashboard } from './pages/cliente-dashboard/cliente-dashboard';
import { DashboardNana } from './pages/nana-dashboard/nana-dashboard';
import { Reservar } from './pages/reservar/reservar';
import { PerfilCliente } from './pages/perfil-cliente/perfil-cliente';
import { Favoritos } from './pages/favoritos/favoritos';
import { VerperfilNana } from './pages/verperfil-nana/verperfil-nana';

import { UbicacionComponent } from './pages/ubicacion/ubicacion';

export const routes: Routes = [

  { path: '', component: Home },

  {
    path: 'buscar',
    component: Buscar
  },

  {
    path:'verperfil-nana/:id',
    loadComponent: () =>
      import('./pages/verperfil-nana/verperfil-nana')
      .then(m => m.VerperfilNana)
    },

  {
    path: 'reservar',
    component: Reservar
  },

  {
    path: 'favoritos',
    component: Favoritos
  },

  {
    path: 'perfil-cliente',
    component: PerfilCliente
  },

  {
    path: 'como-funciona',
    component: ComoFunciona
  },

  {
    path: 'nosotros',
    component: Nosotros
  },

  {
    path: 'contacto',
    component: Contacto
  },

  {
    path: 'perfil-nana',
    component: PerfilNana
  },

  {
    path: 'registro-nana',
    component: RegistroNana
  },

  {
    path: 'dashboard-nana',
    component: DashboardNana
  },

  {
    path: 'login',
    component: Login
  },

  {
    path: 'registro',
    component: Registro
  },

  {
    path: 'dashboard-admin',
    component: AdminDashboard
  },

  {
    path: 'dashboard-cliente',
    component: ClienteDashboard
  },
   {
  path: 'ubicacion/:idNana',
  component: UbicacionComponent
},

  {
    path: '**',
    redirectTo: ''
  }



];
