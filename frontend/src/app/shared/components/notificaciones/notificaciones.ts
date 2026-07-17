import {
  Component,
  HostListener,
  OnInit,
  inject,
  PLATFORM_ID
} from '@angular/core';

import {
  CommonModule,
  isPlatformBrowser
} from '@angular/common';

import {
  NotificacionService,
  Notificacion
} from '../../../servicios/notificacion';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-notificaciones',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './notificaciones.html',
  styleUrl: './notificaciones.css'
})
export class Notificaciones implements OnInit {

  // Evita el error de localStorage en SSR
  private platformId = inject(PLATFORM_ID);

  mostrar = false;

  notificaciones: Notificacion[] = [];

  constructor(
    private notificacionService: NotificacionService
  ) {}

  ngOnInit(): void {

    // Solo ejecutar en el navegador
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    const usuario = JSON.parse(
      localStorage.getItem('usuario') || '{}'
    );

    if (!usuario.idUsuario) {
      return;
    }

    this.cargarNotificaciones(usuario.idUsuario);

  }

  cargarNotificaciones(idUsuario: number): void {

    this.notificacionService
      .obtener(idUsuario)
      .subscribe({

        next: (data) => {

          this.notificaciones = data;

        },

        error: (err) => {

          console.error('Error cargando notificaciones', err);

        }

      });

  }

  get pendientes(): number {

    return this.notificaciones.filter(n => !n.leida).length;

  }

  toggle(event?: MouseEvent): void {

    if (event) {
      event.stopPropagation();
    }

    this.mostrar = !this.mostrar;

  }

  marcarLeida(n: Notificacion): void {

    if (n.leida) {
      return;
    }

    this.notificacionService
      .marcarLeida(n.idNotificacion)
      .subscribe({

        next: () => {

          n.leida = true;

        },

        error: (err) => {

          console.error(err);

        }

      });

  }

  @HostListener('document:click')
  cerrar(): void {

    this.mostrar = false;

  }

  detener(event: MouseEvent): void {

    event.stopPropagation();

  }

}
