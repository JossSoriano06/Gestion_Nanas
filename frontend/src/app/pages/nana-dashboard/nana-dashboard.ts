import { Component, OnInit, inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';

import { ReservaService } from '../../servicios/reserva';
import { Reserva } from '../../modelos/reserva';

import { Notificaciones } from '../../shared/components/notificaciones/notificaciones';
import { MatIconModule } from '@angular/material/icon';
import Swal from 'sweetalert2';

import { UbicacionService } from '../../servicios/Ubicacion';
import { Ubicacion } from '../../modelos/ubicacion';

@Component({
  selector: 'app-nana-dashboard',
  standalone: true,
  imports: [CommonModule, Notificaciones, MatIconModule],
  templateUrl: './nana-dashboard.html',
  styleUrl: './nana-dashboard.css'
})
export class DashboardNana implements OnInit {
cerrarSesion(): void {

  if (isPlatformBrowser(this.platformId)) {

    localStorage.clear();

  }

  this.router.navigate(['/']);

}

  private platformId = inject(PLATFORM_ID);
  private router = inject(Router);

  constructor(
    private reservaService: ReservaService,
    private ubicacionService: UbicacionService,
    private cd: ChangeDetectorRef
  ) {}

  nombre = '';
  apellido = '';
  correo = '';
  telefono = '';
  universidad = '';
  carrera = '';
  distrito = '';
  tarifa = '';

  solicitudes: Reserva[] = [];

  serviciosActivos: Reserva[] = [];

  private watchId: number | null = null;

private idUsuario = 0;

  ngOnInit(): void {

    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    const usuario = JSON.parse(localStorage.getItem('usuario') || '{}');
    this.idUsuario = usuario.idUsuario;

    this.nombre = usuario.nombre || '';
    this.apellido = usuario.apellido || '';
    this.correo = usuario.correo || '';
    this.telefono = usuario.telefono || '';
    this.universidad = usuario.universidad || '';
    this.carrera = usuario.carrera || '';
    this.distrito = usuario.distrito || '';
    this.tarifa = usuario.tarifa || '';

    const idNana = Number(localStorage.getItem('id_nana'));

    this.reservaService.obtenerReservasNana(idNana)
      .subscribe({

        next: (data) => {

          console.log("RESERVAS:", data);

          // Solicitudes pendientes
         this.solicitudes = data.filter(r =>
    r.estadoReserva === 'PENDIENTE'
      );

      //servicios listos para iniciar
    this.serviciosActivos = data.filter(r =>
    r.estadoReserva === 'ACEPTADA' &&
    r.estadoPago === 'PAGADO'
      );

          this.cd.detectChanges();

        },

        error: (err) => {

          console.error(err);

        }

      });

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

 iniciarServicio(idReserva: number): void {

  Swal.fire({

    title: '¿Iniciar servicio?',

    html: `
      <p>Al iniciar el servicio:</p>

      <ul style="text-align:left; margin-top:10px;">
        <li>✅ El cliente será notificado.</li>
        <li>📍 El cliente podrá visualizar tu ubicación.</li>
        <li>⏱️ La reserva cambiará a <b>EN PROGRESO</b>.</li>
      </ul>
    `,

    icon: 'question',

    showCancelButton: true,

    confirmButtonText: 'Sí, iniciar',

    cancelButtonText: 'Cancelar',

    confirmButtonColor: '#10B981',

    cancelButtonColor: '#64748B'

  }).then((result) => {

    if (!result.isConfirmed) {

      return;

    }

    this.reservaService.iniciarServicio(idReserva)
      .subscribe({

        next: () => {

          Swal.fire({

            icon: 'success',

            title: '¡Servicio iniciado!',

            text: 'El cliente ya puede visualizar tu ubicación.',

            confirmButtonColor: '#2563EB'

          }).then(() => {

            this.ngOnInit();

          });

        },

        error: (err) => {

          console.error(err);

          Swal.fire({

            icon: 'error',

            title: 'No fue posible iniciar el servicio',

            text: 'Inténtalo nuevamente.',

            confirmButtonColor: '#DC2626'

          });

        }

      });

  });

}

}
