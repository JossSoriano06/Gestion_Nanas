import { Component, OnInit, inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

import { ReservaService } from '../../servicios/reserva';
import { Reserva } from '../../modelos/reserva';

@Component({
  selector: 'app-cliente-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink
  ],
  templateUrl: './cliente-dashboard.html',
  styleUrl: './cliente-dashboard.css'
})
export class ClienteDashboard implements OnInit {

  private platformId = inject(PLATFORM_ID);

  nombre = '';
  apellido = '';

  reservas: Reserva[] = [];

  constructor(
    private router: Router,
    private reservaService: ReservaService,
    private cd: ChangeDetectorRef
  ) { }

  ngOnInit(): void {

    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    const usuario = JSON.parse(localStorage.getItem('usuario') || '{}');

    this.nombre = usuario.nombre || '';
    this.apellido = usuario.apellido || '';

    if (!usuario.idCliente) {
      return;
    }

    this.reservaService.obtenerReservasCliente(usuario.idCliente)
      .subscribe({
        next: (data) => {
          console.log("LLEGÓ DEL BACK:", data);

          this.reservas = data;

          this.cd.detectChanges();

          console.log("RESERVAS COMPONENTE:", this.reservas);

        },
        error: (err) => {
          console.error(err);
        }
      });

  }

  cerrarSesion(): void {

    localStorage.clear();

    this.router.navigate(['/']);

  }

}