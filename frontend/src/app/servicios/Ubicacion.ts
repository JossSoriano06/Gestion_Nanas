import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { Ubicacion } from '../modelos/ubicacion';

@Injectable({
  providedIn: 'root'
})
export class UbicacionService {

  private api =
    'http://localhost:8080/api/ubicaciones';

  constructor(
    private http: HttpClient
  ) { }

  guardar(
    ubicacion: Ubicacion
  ): Observable<Ubicacion> {

    return this.http.post<Ubicacion>(
      this.api,
      ubicacion
    );

  }

  obtenerUltima(
    idUsuario: number
  ): Observable<Ubicacion> {

    return this.http.get<Ubicacion>(
      `${this.api}/${idUsuario}`
    );

  }

}
