import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { ReservaService } from '../../servicios/reserva';
import { Reserva } from '../../modelos/reserva';

import { NanaService } from '../../servicios/nana';
import { Nana } from '../../modelos/nana';

import { NavbarCliente } from '../../shared/components/navbar-cliente/navbar-cliente';
import { Footer } from '../../shared/components/footer/footer';

import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Component({
  selector: 'app-verperfil-nana',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    NavbarCliente,
    Footer
  ],
  templateUrl: './verperfil-nana.html',
  styleUrl: './verperfil-nana.css'
})
export class VerperfilNana implements OnInit {

  nana: Nana | null = null;

  cargando = false;

  mostrarFormulario = false;

  fechaInicio = '';

  fechaFin = '';

  montoTotal = 0;

  constructor(
  private route: ActivatedRoute,
  private router: Router,
  private nanaService: NanaService,
  private reservaService: ReservaService,
  private cd: ChangeDetectorRef
) {}

  ngOnInit(): void {

    this.route.paramMap.subscribe(params => {

      const id = Number(params.get('id'));

      console.log('ID NANA:', id);

      this.cargarPerfil(id);

    });

  }

  cargarPerfil(id: number): void {

    this.cargando = true;

    this.nana = null;

    this.nanaService.obtenerPorId(id).subscribe({

      next: (data) => {

        console.log('NANA RECIBIDA:', data);

        this.nana = data;

        this.cargando = false;

        this.cd.detectChanges();

      },

      error: (error) => {

        console.error(error);

        this.cargando = false;

      }

    });

  }

  abrirReserva(): void {

    this.mostrarFormulario = true;

  }

  calcularMonto(): void {

    if (!this.fechaInicio || !this.fechaFin || !this.nana) {

      return;

    }

    const inicio = new Date(this.fechaInicio);

    const fin = new Date(this.fechaFin);

    const horas = (fin.getTime() - inicio.getTime()) / (1000 * 60 * 60);

    if (horas > 0) {

      this.montoTotal = horas * this.nana.tarifaHora;

    } else {

      this.montoTotal = 0;

    }

  }

 confirmarReserva(): void {

  if (!this.nana) {
    return;
  }

  const reserva: Reserva = {

    idCliente: Number(localStorage.getItem('usuario_id')),

    idNana: this.nana.idNana,

    fechaInicio: this.fechaInicio,

    fechaFin: this.fechaFin,

    montoTotal: this.montoTotal

  };

  Swal.fire({
    title: '¿Confirmar reserva?',
    html: `
      <b>Cuidadora:</b> ${this.nana.nombre} ${this.nana.apellido}<br><br>
      <b>Inicio:</b> ${this.fechaInicio}<br>
      <b>Fin:</b> ${this.fechaFin}<br><br>
      <b>Total:</b> S/. ${this.montoTotal}
    `,
    icon: 'question',
    showCancelButton: true,
    confirmButtonText: 'Sí, reservar',
    cancelButtonText: 'Cancelar',
    confirmButtonColor: '#2563eb',
    cancelButtonColor: '#d33'
  }).then((result) => {

    if (!result.isConfirmed) {
      return;
    }

    this.reservaService.crearReserva(reserva).subscribe({

      next: () => {

        Swal.fire({
          icon: 'success',
          title: '¡Reserva enviada!',
          text: 'La solicitud fue enviada correctamente a la cuidadora.',
          confirmButtonText: 'Ver mis reservas'
        }).then(() => {

          this.mostrarFormulario = false;

          this.router.navigate(['/reservar']);

        });

      },

      error: (error) => {

        console.error(error);

        let mensaje = 'Ocurrió un error al registrar la reserva.';

        if (typeof error.error === 'string') {
          mensaje = error.error;
        } else if (error.error?.mensaje) {
          mensaje = error.error.mensaje;
        }

        Swal.fire({
          icon: 'error',
          title: 'No se pudo registrar la reserva',
          text: mensaje,
          confirmButtonText: 'Entendido'
        });

      }

    });

  });

}
}
