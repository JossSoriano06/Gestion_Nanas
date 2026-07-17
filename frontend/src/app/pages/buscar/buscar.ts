import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { NavbarCliente } from '../../shared/components/navbar-cliente/navbar-cliente';
import { Footer } from '../../shared/components/footer/footer';

import { NanaService } from '../../servicios/nana';
import { Nana } from '../../modelos/nana';

import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-buscar',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    NavbarCliente,
    Footer,
    MatIconModule
  ],
  templateUrl: './buscar.html',
  styleUrl: './buscar.css'
})
export class Buscar implements OnInit {

  nanas: Nana[] = [];
  nanasFiltradas: Nana[] = [];

  nombreBusqueda: string = '';
  distritoSeleccionado: string = '';
  universidadSeleccionada: string = '';

  cargando = true;

  constructor(
    private nanaService: NanaService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarNanas();
  }

  cargarNanas(): void {

    this.nanaService.obtenerDisponibles().subscribe({

      next: (data) => {

        console.log("NANAS RECIBIDAS:");
        console.table(data);

        this.nanas = data;
        this.nanasFiltradas = [...data];

        this.cargando = false;

      },

      error: (err) => {

        console.error(err);

        this.cargando = false;

      }

    });

  }

  buscar(): void {

    this.nanasFiltradas = this.nanas.filter(nana => {

      // Buscar por nombre
      const coincideNombre =
        this.nombreBusqueda.trim() === '' ||
        (`${nana.nombre} ${nana.apellido}`)
          .toLowerCase()
          .includes(this.nombreBusqueda.toLowerCase());

      // Buscar por distrito
      const coincideDistrito =
        this.distritoSeleccionado === '' ||
        (nana.distrito &&
         nana.distrito.toLowerCase() ===
         this.distritoSeleccionado.toLowerCase());

      // Buscar por universidad
      const coincideUniversidad =
        this.universidadSeleccionada === '' ||
        nana.idUniversidad === Number(this.universidadSeleccionada);

      return (
        coincideNombre &&
        coincideDistrito &&
        coincideUniversidad
      );

    });

    console.log("Resultado del filtro:");
    console.log(this.nanasFiltradas);

  }

  verPerfil(id: number): void {

    console.log("BOTÓN PRESIONADO");
    console.log("ID NANA:", id);

    this.router.navigate(['/verperfil-nana', id]);

  }

}
