import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Reserva } from '../modelos/reserva';

@Injectable({
  providedIn: 'root'
})
export class ReservaService {

  private api = 'http://localhost:8080/api/reservas';

  constructor(private http: HttpClient) { }

  crearReserva(reserva: Reserva): Observable<Reserva> {

    return this.http.post<Reserva>(this.api, reserva);

  }

  obtenerSolicitudes(idNana: number) {

    return this.http.get<Reserva[]>(`${this.api}/solicitudes/${idNana}`);

  }

  aceptarReserva(idReserva: number) {

    return this.http.patch(`${this.api}/${idReserva}/aceptar`, {});

  }

  rechazarReserva(idReserva: number) {

    return this.http.patch(`${this.api}/${idReserva}/rechazar`, {});

  }

  obtenerReservasCliente(idCliente: number) {
    return this.http.get<Reserva[]>(`${this.api}/cliente/${idCliente}`);
  }

}