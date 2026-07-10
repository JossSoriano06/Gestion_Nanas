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

    console.log(reserva);

    this.reservaService.crearReserva(reserva).subscribe({

      next: (respuesta) => {

        console.log(respuesta);

        alert('✅ Reserva realizada correctamente.');

        this.mostrarFormulario = false;

      },

      error: (error) => {

        console.error(error);

        alert('❌ No se pudo registrar la reserva.');

      }

    });

  }

}