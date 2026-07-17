import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Pago{

    idReserva:number;

    monto:number;

    metodoPago:string;

    numeroTarjeta:string;

    titular:string;

}

@Injectable({
    providedIn:'root'
})
export class PagoService{

    private api="http://localhost:8080/api/pagos";

    constructor(private http:HttpClient){}

    pagar(pago:Pago):Observable<any>{

        return this.http.post(this.api,pago);

    }

}
