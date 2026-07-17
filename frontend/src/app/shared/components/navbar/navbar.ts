import { Component, OnInit, inject, PLATFORM_ID } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatIconModule
  ],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css']
})
export class Navbar implements OnInit {

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

    if(this.rol==='CLIENTE'){

      this.router.navigate(['/dashboard-cliente']);

    }else if(this.rol==='NANA'){

      this.router.navigate(['/dashboard-nana']);

    }else if(this.rol==='ADMIN'){

      this.router.navigate(['/dashboard-admin']);

    }

  }

}
