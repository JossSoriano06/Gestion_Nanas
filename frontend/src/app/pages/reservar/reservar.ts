import { Component, OnInit, inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';

import { NavbarCliente } from '../../shared/components/navbar-cliente/navbar-cliente';
import { Footer } from '../../shared/components/footer/footer';

import { ReservaService } from '../../servicios/reserva';
import { Reserva } from '../../modelos/reserva';


import { FormsModule } from '@angular/forms';
import { PagoService } from '../../servicios/pago';

import { Router } from '@angular/router';

import Swal from 'sweetalert2';
@Component({
  selector: 'app-reservar',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    NavbarCliente,
    Footer
  ],
  templateUrl: './reservar.html',
  styleUrl: './reservar.css'
})
export class Reservar implements OnInit {

  private platformId = inject(PLATFORM_ID);

  reservas: Reserva[] = [];

  // ==========================
  // MODAL DE PAGO
  // ==========================

  mostrarPago = false;

  reservaSeleccionada!: Reserva;

  numeroTarjeta = '';

  titular = '';

  expiracion = '';

  cvv = '';

  constructor(

    private reservaService: ReservaService,

    private pagoService: PagoService,

    private cd: ChangeDetectorRef,
    private router: Router

  ) {}

  verUbicacion(idNana: number): void {

    this.router.navigate(['/ubicacion', idNana]);

}

  ngOnInit(): void {

    if (!isPlatformBrowser(this.platformId)) {

      return;

    }

    this.cargarReservas();

  }

  // ==========================
  // CARGAR RESERVAS
  // ==========================

  cargarReservas(): void {

    const usuario = JSON.parse(
      localStorage.getItem('usuario') || '{}'
    );

    this.reservaService
      .obtenerReservasCliente(usuario.idCliente)
      .subscribe({

        next: (data) => {

          this.reservas = data;

          this.cd.detectChanges();

        },

        error: (err) => {

          console.error(err);

        }

      });

  }

  // ==========================
  // ABRIR MODAL
  // ==========================

  abrirPago(reserva: Reserva): void {

    this.reservaSeleccionada = reserva;

    this.numeroTarjeta = '';

    this.titular = '';

    this.expiracion = '';

    this.cvv = '';

    this.mostrarPago = true;

  }

  // ==========================
  // CANCELAR
  // ==========================

  cancelarPago(): void {

    this.mostrarPago = false;

  }

  // ==========================
  // CONFIRMAR PAGO
  // ==========================

 procesandoPago = false;

confirmarPago(): void {

  if (
    this.numeroTarjeta.trim() === '' ||
    this.titular.trim() === '' ||
    this.expiracion.trim() === '' ||
    this.cvv.trim() === ''
  ) {

   this.mostrarPago = false;

Swal.fire({

    icon:'warning',

    title:'Campos incompletos',

    text:'Completa todos los datos.'

}).then(()=>{

    this.mostrarPago = true;

});

    return;
  }

  this.procesandoPago = true;
  // Cierra el modal de pago
this.mostrarPago = false;

  Swal.fire({

    title: 'Procesando pago...',

    html: `
      <div style="text-align:center">
        <p>Validando tarjeta...</p>
        <p>Conectando con el banco...</p>
        <p>Autorizando operación...</p>
      </div>
    `,

    allowOutsideClick: false,
    allowEscapeKey: false,

    didOpen: () => {
      Swal.showLoading();
    }

  });

  this.pagoService.pagar({

    idReserva: this.reservaSeleccionada.idReserva!,

    monto: this.reservaSeleccionada.montoTotal,

    metodoPago: 'VISA',

    numeroTarjeta: this.numeroTarjeta.replace(/\s/g, ''),

    titular: this.titular

  }).subscribe({

    next: (respuesta) => {

      this.procesandoPago = false;

      // Actualiza inmediatamente la reserva en pantalla
      this.reservaSeleccionada.estadoPago = 'PAGADO';

      this.mostrarPago = false;

      Swal.fire({

        icon: 'success',

        title: '✅ Pago aprobado',

        html: `
          <b>Operación realizada correctamente</b><br><br>

          <strong>Código:</strong><br>
          ${respuesta.codigoAutorizacion ?? 'SANDBOX'}<br><br>

          <strong>Estado:</strong><br>
          PAGADO
        `

      }).then(() => {

        // Recarga las reservas desde el backend
        this.cargarReservas();

      });

    },

    error: (error) => {

      this.procesandoPago = false;

      Swal.fire({

        icon: 'error',

        title: 'Pago rechazado',

        text: error.error || 'Ocurrió un error al procesar el pago.'

      });

    }

  });

}


}
