package com.Nanas.demo.dominio.puertos.salidas;

import com.Nanas.demo.dominio.modelos.Pago;

public interface PagoRepositoryPort {
    Pago guardarPago(Pago pago);

    Pago buscarPorReserva(Integer idReserva);
}
