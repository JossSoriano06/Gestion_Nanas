import { Component, OnInit, inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';

import { ReservaService } from '../../servicios/reserva';
import { Reserva } from '../../modelos/reserva';

@Component({
  selector: 'app-nana-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './nana-dashboard.html',
  styleUrl: './nana-dashboard.css'
})
export class DashboardNana implements OnInit {

  private platformId = inject(PLATFORM_ID);
  private router = inject(Router);

  constructor(
    private reservaService: ReservaService,
    private cd: ChangeDetectorRef
  ) {}

  nombre = '';
  apellido = '';
  correo = '';
  telefono = '';

  solicitudes: Reserva[] = [];

  ngOnInit(): void {

    if (isPlatformBrowser(this.platformId)) {

      const usuario = JSON.parse(localStorage.getItem('usuario') || '{}');

      this.nombre = usuario.nombre || '';
      this.apellido = usuario.apellido || '';
      this.correo = usuario.correo || '';
      this.telefono = usuario.telefono || '';

      const idNana = Number(localStorage.getItem('id_nana'));

      this.reservaService.obtenerSolicitudes(idNana)
        .subscribe({

          next: (data) => {

            console.log("SOLICITUDES:", data);

            this.solicitudes = [...data];

            this.cd.detectChanges();

          },

          error: (err) => {

            console.error(err);

          }

        });

    }

  }

  aceptar(idReserva: number): void {

    this.reservaService.aceptarReserva(idReserva)
      .subscribe({

        next: () => {

          alert("Reserva aceptada");

          this.ngOnInit();

        },

        error: (err) => {

          console.error(err);

        }

      });

  }

  rechazar(idReserva: number): void {

    this.reservaService.rechazarReserva(idReserva)
      .subscribe({

        next: () => {

          alert("Reserva rechazada");

          this.ngOnInit();

        },

        error: (err) => {

          console.error(err);

        }

      });

  }

  cerrarSesion(): void {

    if (isPlatformBrowser(this.platformId)) {

      localStorage.clear();

    }

    this.router.navigate(['/']);

  }
}
