import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Notificacion{

  idNotificacion:number;

  titulo:string;

  mensaje:string;

  tipo:string;

  fecha:string;

  leida:boolean;

}

@Injectable({
  providedIn:'root'
})
export class NotificacionService{

  private api="http://localhost:8080/api/notificaciones";

  constructor(private http:HttpClient){}

  obtener(idUsuario:number):Observable<Notificacion[]>{

      return this.http.get<Notificacion[]>(
          `${this.api}/${idUsuario}`);

  }

  marcarLeida(id:number){

      return this.http.patch(
          `${this.api}/${id}/leida`,
          {});

  }

}
