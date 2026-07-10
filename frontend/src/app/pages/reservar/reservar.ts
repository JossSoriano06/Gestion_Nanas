import { Component, OnInit, inject, PLATFORM_ID, ChangeDetectorRef } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';

import { NavbarCliente } from '../../shared/components/navbar-cliente/navbar-cliente';
import { Footer } from '../../shared/components/footer/footer';

import { ReservaService } from '../../servicios/reserva';
import { Reserva } from '../../modelos/reserva';

@Component({
  selector: 'app-reservar',
  standalone: true,
  imports: [
    CommonModule,
    NavbarCliente,
    Footer
  ],
  templateUrl: './reservar.html',
  styleUrl: './reservar.css'
})
export class Reservar implements OnInit {

  private platformId = inject(PLATFORM_ID);

  reservas: Reserva[] = [];

  constructor(private reservaService: ReservaService, private cd: ChangeDetectorRef){}

  ngOnInit(): void {

    if(!isPlatformBrowser(this.platformId)){
      return;
    }

    const usuario = JSON.parse(localStorage.getItem('usuario') || '{}');

    console.log(usuario);

    this.reservaService.obtenerReservasCliente(usuario.idCliente)
      .subscribe(data=>{

         console.log("RESERVAS:", data);

        this.reservas = data;

        this.cd.detectChanges();
      });

  }

}