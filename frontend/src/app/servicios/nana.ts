import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Nana } from '../modelos/nana';

@Injectable({
  providedIn: 'root'
})
export class NanaService {
  
  private apiUrl = 'http://localhost:8080/api/nanas';

  constructor(private http: HttpClient) {}

  obtenerDisponibles(): Observable<Nana[]> {
    return this.http.get<Nana[]>(`${this.apiUrl}/disponibles`);
  }

  registrarNana(nana: Nana): Observable<Nana> {
    return this.http.post<Nana>(`${this.apiUrl}/registrar`, nana);
  }

  obtenerPorId(id: number): Observable<Nana> {
    return this.http.get<Nana>(`${this.apiUrl}/${id}`);
  }
}