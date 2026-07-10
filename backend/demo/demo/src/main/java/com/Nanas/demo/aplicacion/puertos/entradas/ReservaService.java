package com.Nanas.demo.aplicacion.puertos.entradas;

import java.util.List;

import com.Nanas.demo.dominio.modelos.Reserva;
import com.Nanas.demo.dominio.modelos.Ubicacion;

public interface ReservaService {
    
    Reserva solicitarReserva(Reserva reserva);
    Ubicacion obtenerUbicacionActualNana(Integer idUsuarioNana);

    void aceptarReserva(Integer idReserva);
    void rechazarReserva(Integer idReserva);
    void iniciarServicio(Integer idReserva);
    void finalizarServicio(Integer idReserva);
    void pagarReserva(Integer idReserva);
    void calificarServicio(Integer idReserva, Integer rating, String comentario);

    Ubicacion buscarUltimaUbicacionUsuario(Integer idUsuario);

    List<Reserva> obtenerPendientesNana(Integer idNana);

    List<Reserva> obtenerReservasCliente(Integer idCliente);
}
