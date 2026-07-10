import { Component, OnInit, inject, PLATFORM_ID } from '@angular/core';
import { Router } from '@angular/router'; // Quitamos RouterLink de aquí porque ya no se usa en el HTML
import { CommonModule, isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-navbar-cliente',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './navbar-cliente.html',
  styleUrl: './navbar-cliente.css'
})
export class NavbarCliente implements OnInit {

  private platformId = inject(PLATFORM_ID);

  nombre = '';
  rol = '';

  constructor(private router: Router){}

  ngOnInit(): void {
    if(isPlatformBrowser(this.platformId)){
      this.nombre = localStorage.getItem('usuario_nombre') || '';
      this.rol = localStorage.getItem('usuario_rol') || '';
    }
  }

  cerrarSesion(){
    localStorage.clear();
    this.router.navigate(['/']);
  }

  irDashboard(){
    if(this.rol === 'CLIENTE'){
      this.router.navigate(['/dashboard-cliente']);
    } else if(this.rol === 'NANA'){
      this.router.navigate(['/dashboard-nana']);
    } else if(this.rol === 'ADMIN'){
      this.router.navigate(['/dashboard-admin']);
    }
  }
}