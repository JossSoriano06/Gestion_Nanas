import {
  AfterViewInit,
  Component,
  OnInit,
  PLATFORM_ID,
  inject
} from '@angular/core';

import {
  CommonModule,
  isPlatformBrowser,
  Location
} from '@angular/common';

import { ActivatedRoute } from '@angular/router';

import { Nana } from '../../modelos/nana';
import { NanaService } from '../../servicios/nana';
import { NavbarCliente } from "../../shared/components/navbar-cliente/navbar-cliente";

import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-ubicacion',
  standalone: true,
  imports: [
    CommonModule,
    NavbarCliente,
    MatIconModule
  ],
  templateUrl: './ubicacion.html',
  styleUrl: './ubicacion.css'
})
export class UbicacionComponent implements OnInit, AfterViewInit {

  private platformId = inject(PLATFORM_ID);

  nana: Nana | null = null;

  private map: any;

  constructor(
    private nanaService: NanaService,
    private route: ActivatedRoute,
    private location: Location
  ) { }

  // ============================
  // Cargar información de la nana
  // ============================

  ngOnInit(): void {

    const parametro = this.route.snapshot.paramMap.get('idNana');

    console.log('Parámetro recibido:', parametro);

    const idNana = Number(parametro);

    console.log('ID convertido:', idNana);

    if (isNaN(idNana)) {

      console.error('ID inválido');

      return;

    }

    this.nanaService.obtenerPorId(idNana)
      .subscribe({

        next: (respuesta) => {

          console.log('Nana recibida:', respuesta);

          this.nana = respuesta;

          // Si el mapa ya existe, actualizar marcador
          if (this.map) {
            this.cargarUbicacion();
          }

        },

        error: (err) => {

          console.error(err);

        }

      });

  }

  // ============================
  // Crear mapa
  // ============================

  async ngAfterViewInit(): Promise<void> {

    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    const L = await import('leaflet');

    this.map = L.map('map').setView(
      [-12.0464, -77.0428],
      13
    );

    L.tileLayer(
      'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
      {
        maxZoom: 19,
        attribution: '&copy; OpenStreetMap'
      }
    ).addTo(this.map);

    // Si la nana ya llegó antes de crear el mapa
    if (this.nana) {
      this.cargarUbicacion();
    }

  }

  // ============================
  // Mostrar ubicación
  // ============================

  async cargarUbicacion() {

    const L = await import('leaflet');

    this.map.eachLayer((layer: any) => {

      if (layer instanceof L.Marker) {
        this.map.removeLayer(layer);
      }

    });

    const lat =
      this.nana?.latitud && this.nana.latitud !== 0
        ? this.nana.latitud
        : -12.0464;

    const lng =
      this.nana?.longitud && this.nana.longitud !== 0
        ? this.nana.longitud
        : -77.0428;

    this.map.setView([lat, lng], 15);

    L.marker([lat, lng])
      .addTo(this.map)
      .bindPopup(
        ` ${this.nana?.nombre} ${this.nana?.apellido}`
      )
      .openPopup();

  }

  volver(): void {

    this.location.back();

  }

}
